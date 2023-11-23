package com.example.books.core.domain.collection

fun interface AddBooksToCurrentUserCollection {
    fun addBooksToCurrentUserCollection(bookIds: Collection<Long>)
}