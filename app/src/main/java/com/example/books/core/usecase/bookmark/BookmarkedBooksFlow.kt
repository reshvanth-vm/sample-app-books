package com.example.books.core.usecase.bookmark

import androidx.paging.*
import com.example.books.core.model.Book
import kotlinx.coroutines.flow.Flow

fun interface BookmarkedBooksFlow {
  fun bookmarkedBooksFlow(userId: Long, config: PagingConfig): Flow<PagingData<Book>>
}