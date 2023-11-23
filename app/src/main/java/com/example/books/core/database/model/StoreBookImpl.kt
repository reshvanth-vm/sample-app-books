package com.example.books.core.database.model

import com.example.books.core.model.StoreBook

data class StoreBookImpl(
  override val id: Long,
  override val coverUrl: String,
  override val isBookmarked: Boolean,
  override val isCollected: Boolean,
) : StoreBook