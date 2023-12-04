package com.example.books.core.usecase.collection

fun interface AddBooksToUserCollection {
    fun addBooksToUserCollection(userId: Long, bookIds: Collection<Long>)
}

