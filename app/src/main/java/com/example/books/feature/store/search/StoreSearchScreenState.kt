package com.example.books.feature.store.search

sealed interface StoreSearchScreenState {
  data object Init : StoreSearchScreenState
  data object Searching : StoreSearchScreenState
  data object EmptySearch : StoreSearchScreenState
}