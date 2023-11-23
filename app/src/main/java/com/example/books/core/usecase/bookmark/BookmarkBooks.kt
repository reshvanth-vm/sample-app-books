package com.example.books.core.usecase.bookmark

fun interface BookmarkBooks {
    fun bookmarkBooks(userId: Long, bookIds: Collection<Long>)
}
