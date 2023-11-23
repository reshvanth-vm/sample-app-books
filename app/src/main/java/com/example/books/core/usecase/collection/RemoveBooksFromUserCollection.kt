package com.example.books.core.usecase.collection

fun interface RemoveBooksFromUserCollection {
  fun removeBooksFromUserCollection(userId: Long, bookIds: Collection<Long>)
}

