package com.example.books.core.domain.profile

import com.example.books.core.usecase.profile.*
import javax.inject.Inject

class NewUserAccountValidator @Inject constructor(
  private val emailValidator: NewUserEmailValidator,
  private val pwdValidator: NewUserPwdValidator,
) : NewUserEmailValidator by emailValidator,
    NewUserPwdValidator by pwdValidator {

  sealed interface InvalidReason {
    @JvmInline
    value class Email(val reason: NewUserEmailValidator.InvalidReason) : InvalidReason
    @JvmInline
    value class Pwd(val reason: NewUserPwdValidator.InvalidReason) : InvalidReason
  }

  suspend fun validate(email: CharSequence?, pwd: CharSequence?): InvalidReason? {
    validateUserEmail(email)?.let { return InvalidReason.Email(it) }
    validateNewPwd(pwd)?.let { return InvalidReason.Pwd(it) }
    return null
  }

}