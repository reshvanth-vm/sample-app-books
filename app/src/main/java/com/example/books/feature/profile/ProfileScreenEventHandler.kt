package com.example.books.feature.profile

import android.view.View

interface ProfileScreenEventHandler {
  fun goBack()
  fun signOut(btn: View)
}