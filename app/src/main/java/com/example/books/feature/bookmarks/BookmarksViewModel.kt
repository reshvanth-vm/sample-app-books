package com.example.books.feature.bookmarks

import androidx.lifecycle.*
import androidx.paging.*
import com.example.books.core.domain.bookmarks.CurrentUserBookmarksFlow
import com.example.books.core.domain.collection.CurrentUserCollectionFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
  currentUserBookmarksFlow: CurrentUserBookmarksFlow,
) : ViewModel() {

  val books = currentUserBookmarksFlow(config).cachedIn(viewModelScope)

  companion object {
    private val config = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      prefetchDistance = 4,
      enablePlaceholders = false,
    )
  }

}