package com.example.books.core.usecase.profile

import com.example.books.core.model.User
import kotlinx.coroutines.flow.StateFlow

fun interface CurrentUserStateFlow {
  fun userStateFlow(): StateFlow<User?>
}

