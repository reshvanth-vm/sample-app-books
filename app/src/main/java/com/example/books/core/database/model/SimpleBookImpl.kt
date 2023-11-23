package com.example.books.core.database.model

import com.example.books.core.model.SimpleBook

data class SimpleBookImpl(
  override val id: Long,
  override val coverUrl: String,
  override val name: String,
  override val author: String,
) : SimpleBook


