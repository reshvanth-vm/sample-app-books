package com.example.books.core.usecase

import kotlinx.coroutines.flow.Flow

fun interface IsUserBookmarkedBookFlow {
    fun isUserBookmarkedBookFlow(userId: Long, bookId: Long): Flow<Boolean>
}