package com.example.books.feature.user.profile.create

import android.util.Log
import androidx.lifecycle.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.NewUserAccountValidator
import com.example.books.core.model.NewUserCredentials
import com.example.books.core.usecase.profile.*
import com.example.books.core.usecase.util.GlobalScopeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.temporal.TemporalAccessor
import javax.inject.Inject

private val AcctCreationErrorState.isClean
  get() = AcctCreationErrorState.Init == this

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

  val errorState: Flow<AcctCreationErrorState>

  var email: CharSequence? = ssh["typed_user_name"]
  var pwd: CharSequence? = ssh["typed_pwd"]
  var confirmPwd: CharSequence? = null

  val validateEmail: () -> Unit
  val validatePwd: () -> Unit
  val validateConfirmPwd: () -> Unit
  val validateAllAndCreate: () -> Unit

  init {
    errorState = MutableSharedFlow()
    val eState = errorState.stateIn(
      viewModelScope, SharingStarted.Eagerly, AcctCreationErrorState.Init
    )

    val emitErrorState: (AcctCreationErrorState.() -> AcctCreationErrorState) -> AcctCreationErrorState = { updater ->
      val updatedErrorState = updater(eState.value)
      viewModelScope.launch { errorState.emit(updatedErrorState) }
      updatedErrorState
    }

    var vJob: Job? = null
    val validatingScope: (suspend CoroutineScope.() -> Unit) -> () -> Unit = { block ->
      {
        vJob?.cancel()
        vJob = viewModelScope.launch(context = ioDispatcher, block = block)
      }
    }

    validateEmail = validatingScope {
      val reason = validator.validateUserEmail(email)
      emitErrorState { copy(email = reason) }
    }

    validatePwd = validatingScope {
      val reason = validator.validateNewPwd(pwd)
      emitErrorState { copy(pwd = reason) }
    }

    validateConfirmPwd = validatingScope {
      if (eState.value.pwd != null) {
        val isConfirmPwdMatched = confirmPwd != pwd
        emitErrorState { copy(confirmPwdNotMatched = isConfirmPwdMatched.not()) }
      }
    }

    validateAllAndCreate = {
      globalScopeProvider.getGlobalScope().launch(ioDispatcher) {
        val result = validator.validate(email, pwd)
        val newErrorState = emitErrorState {
          when (result) {
            is NewUserAccountValidator.InvalidReason.Email -> copy(email = result.reason)
            is NewUserAccountValidator.InvalidReason.Pwd   -> copy(pwd = result.reason)
            null                                           -> if (confirmPwd == this@CreateProfileViewModel.pwd) {
              AcctCreationErrorState.Init
            } else {
              copy(confirmPwdNotMatched = true)
            }
          }
        }

        if (newErrorState.isClean) {
          val newUserAccount = object : NewUserCredentials {
            override val email = this@CreateProfileViewModel.email.toString()
            override val pwd = this@CreateProfileViewModel.pwd.toString()
            override val name = email
            override val profileUri = null
          }

          signUp(newUserAccount).also(signInUser::signInUser)
        }
      }
    }
  }
}
//data class ErrorState(
//  val email: NewUserEmailValidator.InvalidReason?,
//  val pwd: NewUserPwdValidator.InvalidReason?,
//) {
//  companion object {
//    //      val Init = ErrorState(email = null, pwd = null)
//    val None = ErrorState(email = null, pwd = null)
//  }
//}
//
//sealed interface ScreenState {
//  data object SigningIn : ScreenState
//  data object Init : ScreenState
//  @JvmInline
//  value class ProcessingAccountCreation(val state: TransactionState) : ScreenState
//
//  enum class TransactionState { SUCCESS, FAILED, IN_PROGRESS }
//}
//
//data class ActionsState(
//  val canCreateAccount: Boolean,
//) {
//  companion object {
//    val Init = ActionsState(canCreateAccount = false)
//  }
//}
//


//  onEmailOrPwdChange = {
//    screenState.value = ScreenState.Init
//    viewModelScope.launch {
//      delay(100)
//      val result = validator.validate(email, pwd)
//      val newErrorState = errorState.updateAndGet { eState ->
//        when (result) {
//          null                                           -> ErrorState.None
//          is NewUserAccountValidator.InvalidReason.Email -> {
//            eState.copy(email = result.reason, pwd = null)
//          }
//          is NewUserAccountValidator.InvalidReason.Pwd   -> {
//            eState.copy(pwd = result.reason, email = null)
//          }
//        }
//      }
//      actionsState.update { it.copy(canCreateAccount = newErrorState == ErrorState.None) }
//    }
//  }
//
//  var newUser: User? = null
//  createUserAccount = {
//    screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.IN_PROGRESS)
//    globalScopeProvider.getGlobalScope().launch(ioDispatcher) {
//      delay(100)
//      val newUserAccount = object : NewUserCredentials {
//        override val email = this@CreateProfileViewModel.email.toString()
//        override val pwd = this@CreateProfileViewModel.pwd.toString()
//        override val name = email
//        override val profileUri = null
//      }
//      newUser = runCatching { signUp(newUserAccount) }.onSuccess {
//        screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.SUCCESS)
//      }.onFailure {
//        screenState.value = ScreenState.ProcessingAccountCreation(state = ScreenState.TransactionState.FAILED)
//      }.getOrNull()
//    }
//  }
//
//  signIn = {
//    screenState.value = ScreenState.SigningIn
//    signInUser.signInUser(newUser ?: throw RuntimeException())
//  }
//}
//
////  private val _state = MutableStateFlow(CreateProfileUIState.Default)
////  val state = _state.asStateFlow()
////
////  private var eJob: Job? = null
////  override fun onEmailChanged(text: CharSequence?) {
////    eJob?.cancel()
////    eJob = viewModelScope.launch {
////      val invalidReason = validator.validateUserEmail(text)
////      _state.update {
////        it.copy(email = text, emailInvalidReason = invalidReason)
////      }
////    }
////  }
////
////  private var pJob: Job? = null
////  override fun onPwdChanged(text: CharSequence?) {
////    pJob?.cancel()
////    pJob = viewModelScope.launch {
////      val invalidReason = validator.validateNewPwd(text)
////      _state.update {
////        it.copy(pwd = text, pwdInvalidReason = invalidReason)
////      }
////    }
////  }
