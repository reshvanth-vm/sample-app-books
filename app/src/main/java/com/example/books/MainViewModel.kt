package com.example.books

import androidx.lifecycle.ViewModel
import com.example.books.common.core.coroutine.*
import com.example.books.core.domain.profile.GetCurrentUser
import com.example.books.core.usecase.profile.SignInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  userProvider: GetCurrentUser,
  signInUser: SignInUser,
  @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel(),
    SignInUser by signInUser {

  val isUserLoggedInFlow = userProvider
    .flow()
    .mapLatest { it != null }
    .distinctUntilChanged()
    .flowOn(ioDispatcher)

}