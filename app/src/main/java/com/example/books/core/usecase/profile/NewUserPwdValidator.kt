package com.example.books.core.usecase.profile

fun interface NewUserPwdValidator {
  suspend fun validateNewPwd(pwd: CharSequence?): InvalidReason?

  enum class InvalidReason {
    BLANK,
    LONG,
    SHORT,
  }
}

