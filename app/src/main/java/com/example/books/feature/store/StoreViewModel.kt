package com.example.books.feature.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.books.core.model.*
import com.example.books.core.usecase.store.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
//  storeBooksPagingDataFlow: StoreBooksPagingDataFlow,
  storeBooksPagingDataFlow: StoreBooksPagingDataFlow,
  userStoreBooksPagingDataFlow: UserStoreBooksPagingDataFlow,
) : ViewModel() {

  val books: Flow<PagingData<com.example.books.core.model.BookCover>> = storeBooksPagingDataFlow
    .storeBookCoversPagingDataFlow(pagingConfig)
    .cachedIn(viewModelScope)

  val userBooks = userStoreBooksPagingDataFlow
    .userStoreBooksPagingDataFlow(pagingConfig)
    .cachedIn(viewModelScope)

  companion object {
    private val pagingConfig = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      prefetchDistance = 0,
      enablePlaceholders = true,
//      maxSize = 50,
//      jumpThreshold = 20,
    )
  }

}