package com.example.books.core.usecase.profile

import com.example.books.core.model.*

fun interface SignUpUser {
  suspend fun signUp(credentials: NewUserCredentials): User
}

