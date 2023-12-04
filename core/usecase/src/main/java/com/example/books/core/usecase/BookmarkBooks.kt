package com.example.books.core.usecase

fun interface BookmarkBooks {
    fun bookmarkBooks(userId: Long, bookIds: Collection<Long>)
}
