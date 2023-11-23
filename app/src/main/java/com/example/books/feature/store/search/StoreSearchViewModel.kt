package com.example.books.feature.store.search

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.books.core.model.BookCover
import com.example.books.core.usecase.store.StoreBooksSearchPagingDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StoreSearchViewModel @Inject constructor(
//  searchStoreObservable: SearchStoreObservable,
  storeBooksSearchPagingDataFlow: StoreBooksSearchPagingDataFlow,
) : ViewModel(),
    StoreBooksSearchPagingDataFlow by storeBooksSearchPagingDataFlow {

  private val queryFlow = MutableStateFlow<CharSequence?>(null)

  var currentSearchQuery: CharSequence?
    get() = queryFlow.value
    set(value) {
      queryFlow.value = value
    }

  val screenState: StateFlow<StoreSearchScreenState>
  val searchFlow: Flow<PagingData<BookCover>>

  init {
    screenState = MutableStateFlow<StoreSearchScreenState>(StoreSearchScreenState.Init)

    searchFlow = queryFlow.flatMapLatest {
      if (it.isNullOrBlank()) {
        screenState.value = StoreSearchScreenState.Init
        flowOf(PagingData.empty())
      } else {
        screenState.value = StoreSearchScreenState.Searching
        delay(300)
        storeBookCoversSearchPagingDataFlowMatching(it.toString(), config).onStart {
          screenState.value = StoreSearchScreenState.Init
        }
      }
    }.onStart {
      Log.e("search", "search paging state **********")
    }.cachedIn(viewModelScope)

  }

  companion object {
    private val config = PagingConfig(
      pageSize = 10,
      prefetchDistance = 4,
      maxSize = 50,
    )
  }

}

