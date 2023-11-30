package com.example.books.feature.user.profile.create

import com.example.books.core.usecase.profile.*

data class AcctCreationErrorState(
  val email: NewUserEmailValidator.InvalidReason?,
  val pwd: NewUserPwdValidator.InvalidReason?,
  val confirmPwdNotMatched: Boolean,
) {

  companion object {
    val Init = AcctCreationErrorState(
      email = null,
      pwd = null,
      confirmPwdNotMatched = false,
    )
  }

}