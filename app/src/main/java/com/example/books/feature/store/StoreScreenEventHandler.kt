package com.example.books.feature.store

import android.view.*

interface StoreScreenEventHandler {
  fun navigateToStoreSearchScreen(view: View)
  fun navigateToProfileScreen(menuItem: MenuItem)
}