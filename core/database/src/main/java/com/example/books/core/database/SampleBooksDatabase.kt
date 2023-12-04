package com.example.books.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.books.core.database.dao.*
import com.example.books.core.database.entity.*


@Database(
  entities = [
    BookEntity::class,
    UserEntity::class,
    UserBookmarkedBookEntity::class,
    UserBookEntity::class,
    UserPrefsEntity::class,
  ],
  version = 1,
  exportSchema = true,
)
abstract class SampleBooksDatabase : RoomDatabase() {

  abstract fun bookDao(): BookDao
  abstract fun userDao(): UserDao

  companion object {
    internal const val FILE_NAME = "sample_books"
  }

}