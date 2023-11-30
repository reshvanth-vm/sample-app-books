package com.example.books.feature.user.profile

import androidx.lifecycle.*
import com.example.books.core.usecase.profile.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  signOutUser: SignOutUser,
  userFlow: CurrentUserFlow,
) : ViewModel(),
    SignOutUser by signOutUser {

  val email = userFlow
    .currentUserFlow()
    .mapLatest { it.email }
    .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

}