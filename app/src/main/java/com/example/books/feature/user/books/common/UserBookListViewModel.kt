package com.example.books.feature.user.books.common

import androidx.lifecycle.*
import androidx.paging.*
import com.example.books.core.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

abstract class UserBookListViewModel : ViewModel() {

  val booksFlow: Flow<PagingData<Book>> by lazy {
    getBooksFlow(config).cachedIn(viewModelScope)
  }

  protected abstract fun getBooksFlow(config: PagingConfig): Flow<PagingData<Book>>

  companion object {
    private val config = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      prefetchDistance = 4,
      enablePlaceholders = false,
    )
  }
}