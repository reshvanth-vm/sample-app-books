package com.example.books

import androidx.lifecycle.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
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
) : ViewModel(), SignInUser by signInUser {

  val isUserLoggedInFlow = userProvider
    .flow()
    .mapLatest { it != null }
    .distinctUntilChanged()
    .flowOn(ioDispatcher)

}