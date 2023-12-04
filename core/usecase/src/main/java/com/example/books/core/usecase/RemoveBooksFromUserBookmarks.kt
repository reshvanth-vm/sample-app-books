package com.example.books.core.usecase

fun interface RemoveBooksFromUserBookmarks {
  fun removeBooksFromUserBookmarks(userId: Long, bookIds: Collection<Long>)
}