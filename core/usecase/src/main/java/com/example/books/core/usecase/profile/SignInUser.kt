package com.example.books.core.usecase.profile

import com.example.books.core.model.User

fun interface SignInUser {
  fun signInUser(user: User)
}

