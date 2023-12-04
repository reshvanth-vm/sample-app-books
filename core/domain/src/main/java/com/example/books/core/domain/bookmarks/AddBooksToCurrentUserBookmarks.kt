package com.example.books.core.domain.bookmarks

fun interface AddBooksToCurrentUserBookmarks {
  fun bookmarkBooks(bookIds: Collection<Long>)
}