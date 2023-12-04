package com.example.books.core.usecase.profile

fun interface NewUserPwdValidator {
  suspend fun validateNewPwd(pwd: CharSequence?): InvalidReason?

  enum class InvalidReason {
    BLANK,
    // 2 upper, 2 lower, 2 digits, 2 special
    NOT_MATCHED_CONDITION,
//    LONG,
//    SHORT,
  }
}

