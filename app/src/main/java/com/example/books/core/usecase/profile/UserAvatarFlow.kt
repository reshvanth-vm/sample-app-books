package com.example.books.core.usecase.profile

import kotlinx.coroutines.flow.Flow

fun interface UserAvatarFlow {
  fun userAvatarFlow(): Flow<String?>
}

