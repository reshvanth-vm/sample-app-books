package com.example.books.core.database.entity

import androidx.room.*
import com.example.books.core.model.DetailedBook

@Entity(tableName = BookEntity.TABLE_NAME)
data class BookEntity(
  @PrimaryKey(autoGenerate = true) override val id: Long,
  override val coverUrl: String,
  override val name: String,
  override val author: String,
  override val pagesCount: Int,
  override val description: String,
  override val publisher: String,
  override val publishedDate: String,
  override val rating: Float,
  override val ratingCount: Int,
  val price: Float,
) : DetailedBook {
  companion object {
    const val TABLE_NAME = "book"
  }
}
