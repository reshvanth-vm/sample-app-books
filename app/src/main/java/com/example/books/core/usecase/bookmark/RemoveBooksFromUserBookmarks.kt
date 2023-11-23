package com.example.books.core.usecase.bookmark

fun interface RemoveBooksFromUserBookmarks {
  fun removeBooksFromUserBookmarks(userId: Long, bookIds: Collection<Long>)
}