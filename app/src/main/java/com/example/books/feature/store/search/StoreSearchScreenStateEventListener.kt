package com.example.books.feature.store.search

import android.text.Editable

interface StoreSearchScreenStateEventListener {
  fun afterSearchQueryTextChanged(text: Editable?)
}