package com.example.books.core.usecase.collection

import androidx.paging.*
import com.example.books.core.model.Book
import kotlinx.coroutines.flow.Flow

fun interface CollectionBooksFlow {
  fun collectionBooksFlow(userId: Long, config: PagingConfig): Flow<PagingData<Book>>
}
