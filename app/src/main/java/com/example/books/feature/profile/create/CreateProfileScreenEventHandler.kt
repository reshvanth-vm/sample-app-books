package com.example.books.feature.profile.create

import android.text.Editable

interface CreateProfileScreenEventHandler {
  fun cancelAccountCreation()
  fun afterEmailTextChanged(e: Editable?)
  fun afterPwdTextChanged(e: Editable?)
  fun createAccount()
}