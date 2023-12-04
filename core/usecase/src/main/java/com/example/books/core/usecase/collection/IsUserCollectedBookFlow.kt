package com.example.books.core.usecase.collection

import kotlinx.coroutines.flow.Flow

fun interface IsUserCollectedBookFlow {
    fun isUserCollectedBookFlow(userId: Long, bookId: Long): Flow<Boolean>
}