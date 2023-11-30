package com.example.books.feature.user.login

import androidx.lifecycle.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.usecase.profile.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userAuthenticator: UserAuthenticator,
  private val signInUser: SignInUser,
  getRecentlyLoggedInUserEmails: GetRecentlyLoggedInUserEmails,
  @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

  private val _auth = MutableSharedFlow<AuthenticationStep>(replay = 0)
  val authFlow: Flow<AuthenticationStep> = _auth

  var email: CharSequence? = null
  var pwd: CharSequence? = null
  val emailSuggestions = flow {
    emit(getRecentlyLoggedInUserEmails.getRecentlyLoggedInUserEmail())
  }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

  fun signIn(email: CharSequence?, pwd: CharSequence?) {
    viewModelScope.launch(ioDispatcher) {
      _auth.emit(AuthenticationStep.Authenticating)
      when (val result = userAuthenticator.authenticateUser(email, pwd)) {
        is UserAuthenticator.Result.Error   -> _auth.emit(AuthenticationStep.Error(result.invalidReason))
        is UserAuthenticator.Result.Success -> {
          _auth.emit(AuthenticationStep.Success)
          signInUser.signInUser(result.user)
        }
      }
    }
  }

}

//interface LoginScreenStateHolder {
//  val email: CharSequence?
//  val pwd: CharSequence?
//  val error: UserAuthenticator.InvalidReason?
//}

//interface LoginScreenStateChangeObserver {
//  fun onEmailTxtChanged(e: Editable?)
//  fun onPwdTxtChanged(e: Editable?)
//}
//
//sealed interface LoginScreenAction {
//  data object SignIn : LoginScreenAction
//  data object SignUp : LoginScreenAction
//
//  fun interface Listener {
//    fun onAction(action: LoginScreenAction)
//  }
//}

