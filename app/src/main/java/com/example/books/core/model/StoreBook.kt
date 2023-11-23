package com.example.books.core.model

interface StoreBook : BookCover {
  val isBookmarked: Boolean
  val isCollected: Boolean
}