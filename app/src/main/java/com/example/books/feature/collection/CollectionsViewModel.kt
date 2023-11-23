package com.example.books.feature.collection

import androidx.lifecycle.*
import androidx.paging.*
import com.example.books.core.domain.collection.CurrentUserCollectionFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
  currentUserCollectionBooksFlow: CurrentUserCollectionFlow,
) : ViewModel() {

  val books = currentUserCollectionBooksFlow(config).cachedIn(viewModelScope)

  companion object {
    private val config = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      prefetchDistance = 4,
      enablePlaceholders = false,
    )
  }

}