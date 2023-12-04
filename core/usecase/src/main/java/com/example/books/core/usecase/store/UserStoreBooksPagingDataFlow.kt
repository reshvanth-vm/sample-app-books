package com.example.books.core.usecase.store

import androidx.paging.*
import com.example.books.core.model.StoreBook
import kotlinx.coroutines.flow.Flow

fun interface UserStoreBooksPagingDataFlow {
  fun userStoreBooksPagingDataFlow(pagingConfig: PagingConfig): Flow<PagingData<StoreBook>>
}