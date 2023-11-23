package com.example.books.core.database.entity

import androidx.room.Entity

@Entity(tableName = UserBookmarkedBookEntity.TABLE_NAME, primaryKeys = ["userId", "bookId"])
data class UserBookmarkedBookEntity(
    val userId: Long,
    val bookId: Long,
    val bookmarkedDate: Long,
) {
    companion object {
        const val TABLE_NAME = "user_bookmarked_book"
    }
}