package com.example.books.core.model

interface Book : SimpleBook {
  val publisher: String
  val rating: Float
}

