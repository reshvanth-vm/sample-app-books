package com.example.books.core.model

interface DetailedBook : SimpleBook {
  val pagesCount: Int
  val description: String
  val publisher: String
  val publishedDate: String
  val rating: Float
  val ratingCount: Int
}

