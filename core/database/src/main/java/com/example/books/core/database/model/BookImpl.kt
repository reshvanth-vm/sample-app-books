package com.example.books.core.database.model

import com.example.books.core.model.Book

data class BookImpl(
  override val id: Long,
  override val coverUrl: String,
  override val name: String,
  override val author: String,
  override val publisher: String,
  override val rating: Float,
) : com.example.books.core.model.Book