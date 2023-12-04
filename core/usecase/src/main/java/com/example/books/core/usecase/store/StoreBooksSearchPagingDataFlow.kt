package com.example.books.core.usecase.store

import androidx.paging.*
import com.example.books.core.model.BookCover
import kotlinx.coroutines.flow.Flow

fun interface StoreBooksSearchPagingDataFlow {
    fun storeBookCoversSearchPagingDataFlowMatching(txt: String, config: PagingConfig): Flow<PagingData<BookCover>>
}