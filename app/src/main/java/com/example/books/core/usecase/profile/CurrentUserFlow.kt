package com.example.books.core.usecase.profile

import com.example.books.core.model.User
import kotlinx.coroutines.flow.Flow

fun interface CurrentUserFlow {
  fun currentUserFlow(): Flow<User>
}