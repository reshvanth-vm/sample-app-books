package com.example.books.core.domain.bookmarks

fun interface RemoveBooksFromCurrentUserBookmarks {
    fun removeBooksFromCurrentUserBookmarks(bookIds: Collection<Long>)
}