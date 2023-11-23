package com.example.books.feature.profile.create

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LOG_TAG
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.NewUserAccountValidator
import com.example.books.core.model.*
import com.example.books.core.usecase.profile.*
import com.example.books.core.usecase.util.GlobalScopeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
  ssh: SavedStateHandle,
  signInUser: SignInUser,
  signUpUser: SignUpUser,
  validator: NewUserAccountValidator,
  globalScopeProvider: GlobalScopeProvider,
  @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel(),
    SignUpUser by signUpUser {

  data class ErrorState(
    val email: NewUserEmailValidator.InvalidReason?,
    val pwd: NewUserPwdValidator.InvalidReason?,
  ) {
    companion object {
      //      val Init = ErrorState(email = null, pwd = null)
      val None = ErrorState(email = null, pwd = null)
    }
  }

  sealed interface ScreenState {
    data object SigningIn : ScreenState
    data object Init : ScreenState
    @JvmInline
    value class ProcessingAccountCreation(val state: TransactionState) : ScreenState

    enum class TransactionState { SUCCESS, FAILED, IN_PROGRESS }
  }

  data class ActionsState(
    val canCreateAccount: Boolean,
  ) {
    companion object {
      val Init = ActionsState(canCreateAccount = false)
    }
  }

  // states
  val errorState: StateFlow<ErrorState>
  val screenState: StateFlow<ScreenState>
  val actionsState: StateFlow<ActionsState>

  // actions
  private val onEmailOrPwdChange: () -> Unit
  val createUserAccount: () -> Unit
  val signIn: () -> Unit

  var email: CharSequence? = ssh["typed_user_name"]
    set(value) {
      field = value
      onEmailOrPwdChange()
    }

  var pwd: CharSequence? = ssh["typed_pwd"]
    set(value) {
      field = value
      onEmailOrPwdChange()
    }

  init {
    errorState = MutableStateFlow(ErrorState.None)
    screenState = MutableStateFlow<ScreenState>(ScreenState.Init)
    actionsState = MutableStateFlow(ActionsState.Init)

    onEmailOrPwdChange = {
      screenState.value = ScreenState.Init
      viewModelScope.launch {
        delay(100)
        val result = validator.validate(email, pwd)
        val newErrorState = errorState.updateAndGet { eState ->
          when (result) {
            null                                           -> ErrorState.None
            is NewUserAccountValidator.InvalidReason.Email -> {
              eState.copy(email = result.reason, pwd = null)
            }
            is NewUserAccountValidator.InvalidReason.Pwd   -> {
              eState.copy(pwd = result.reason, email = null)
            }
          }
        }
        actionsState.update { it.copy(canCreateAccount = newErrorState == ErrorState.None) }
      }
    }

    var newUser: User? = null
    createUserAccount = {
      screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.IN_PROGRESS)
      globalScopeProvider.getGlobalScope().launch(ioDispatcher) {
        delay(100)
        val newUserAccount = object : NewUserCredentials {
          override val email = this@CreateProfileViewModel.email.toString()
          override val pwd = this@CreateProfileViewModel.pwd.toString()
          override val name = email
          override val profileUri = null
        }
        newUser = runCatching { signUp(newUserAccount) }.onSuccess {
          screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.SUCCESS)
        }.onFailure {
          screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.FAILED)
        }.getOrNull()
      }
    }

    signIn = {
      screenState.value = ScreenState.SigningIn
      signInUser.signInUser(newUser ?: throw RuntimeException())
    }
  }

//  private val _state = MutableStateFlow(CreateProfileUIState.Default)
//  val state = _state.asStateFlow()
//
//  private var eJob: Job? = null
//  override fun onEmailChanged(text: CharSequence?) {
//    eJob?.cancel()
//    eJob = viewModelScope.launch {
//      val invalidReason = validator.validateUserEmail(text)
//      _state.update {
//        it.copy(email = text, emailInvalidReason = invalidReason)
//      }
//    }
//  }
//
//  private var pJob: Job? = null
//  override fun onPwdChanged(text: CharSequence?) {
//    pJob?.cancel()
//    pJob = viewModelScope.launch {
//      val invalidReason = validator.validateNewPwd(text)
//      _state.update {
//        it.copy(pwd = text, pwdInvalidReason = invalidReason)
//      }
//    }
//  }

}
