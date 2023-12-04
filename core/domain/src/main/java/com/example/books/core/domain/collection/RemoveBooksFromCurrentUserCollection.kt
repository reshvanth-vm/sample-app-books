package com.example.books.core.domain.collection

fun interface RemoveBooksFromCurrentUserCollection {
    fun removeBooksFromUserCollection(bookIds: Collection<Long>)
}

