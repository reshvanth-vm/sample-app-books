package com.example.books.feature.profile.create

interface CreateProfileStateChangeListener {
  fun onEmailChanged(text: CharSequence?)
  fun onPwdChanged(text: CharSequence?)
}