package com.example.books.core.usecase.common

import com.example.books.core.model.DetailedBook

fun interface GetDetailedBook {
    suspend fun getDetailedBook(bookId: Long): DetailedBook
}

