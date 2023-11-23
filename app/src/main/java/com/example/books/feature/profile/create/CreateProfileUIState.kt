package com.example.books.feature.profile.create

import com.example.books.core.usecase.profile.*

data class CreateProfileUIState(
  val email: CharSequence?,
  val emailInvalidReason: NewUserEmailValidator.InvalidReason?,
  val pwd: CharSequence?,
  val pwdInvalidReason: NewUserPwdValidator.InvalidReason?,
) {
  companion object {
    val Default = CreateProfileUIState(
      email = null,
      emailInvalidReason = null,
      pwd = null,
      pwdInvalidReason = null,
    )
  }
}