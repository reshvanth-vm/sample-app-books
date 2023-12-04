package com.example.books.core.usecase.profile

fun interface NewUserEmailValidator {
  suspend fun validateUserEmail(email: CharSequence?): InvalidReason?

  enum class InvalidReason {
    BLANK,
    EXIST,
    ERROR,
  }

}