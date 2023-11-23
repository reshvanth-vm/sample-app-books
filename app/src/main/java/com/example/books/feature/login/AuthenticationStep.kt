package com.example.books.feature.login

import com.example.books.core.usecase.profile.UserAuthenticator

sealed interface AuthenticationStep {
  data object Authenticating : AuthenticationStep
  data object Success : AuthenticationStep
  @JvmInline
  value class Error(val reason: UserAuthenticator.InvalidReason) : AuthenticationStep
}