package com.example.books.feature.user.books.common

import androidx.lifecycle.*
import androidx.paging.*
import com.example.books.core.model.Book
import kotlinx.coroutines.flow.Flow

abstract class UserBookListViewModel : ViewModel() {

  val booksFlow: Flow<PagingData<com.example.books.core.model.Book>> by lazy {
    getBooksFlow(config).cachedIn(viewModelScope)
  }

  protected abstract fun getBooksFlow(config: PagingConfig): Flow<PagingData<com.example.books.core.model.Book>>

  companion object {
    private val config = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      prefetchDistance = 4,
      enablePlaceholders = false,
    )
  }
}