package com.example.books.core.usecase.bookmark

import kotlinx.coroutines.flow.Flow

fun interface IsUserBookmarkedBookFlow {
    fun isUserBookmarkedBookFlow(userId: Long, bookId: Long): Flow<Boolean>
}