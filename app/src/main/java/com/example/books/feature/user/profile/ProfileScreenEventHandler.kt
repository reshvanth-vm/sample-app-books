package com.example.books.feature.user.profile

import android.view.View

interface ProfileScreenEventHandler {
  fun goBack()
  fun signOut(btn: View)
}