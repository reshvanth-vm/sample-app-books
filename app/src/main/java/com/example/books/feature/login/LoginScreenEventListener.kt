package com.example.books.feature.login

import android.text.Editable
import android.view.View

interface LoginScreenEventListener {
  fun afterEmailTxtChanged(e: Editable?)
  fun afterPwdTxtChanged(e: Editable?)
  fun signIn(btn: View)
  fun signUp(btn: View)
}