package com.example.books.core.database.entity

import androidx.room.Entity

@Entity(tableName = UserBookEntity.TABLE_NAME, primaryKeys = ["userId", "bookId"])
data class UserBookEntity(
    val userId: Long,
    val bookId: Long,
    val collectedDate: Long,
) {
    companion object {
        const val TABLE_NAME = "user_book"
    }
}