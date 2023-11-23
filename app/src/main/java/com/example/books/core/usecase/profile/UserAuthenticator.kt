package com.example.books.core.usecase.profile

import com.example.books.core.model.User

fun interface UserAuthenticator {
  suspend fun authenticateUser(email: CharSequence?, pwd: CharSequence?): Result

  sealed interface Result {
    @JvmInline
    value class Success(val user: User) : Result
    @JvmInline
    value class Error(val invalidReason: InvalidReason) : Result
  }

  enum class InvalidReason {
    BLANK_EMAIL,
    BLANK_PWD,
    EMAIL_NOT_FOUND,
    PWD_NOT_MATCHED,
  }
}

