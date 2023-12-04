package com.example.books.feature.user.books.bookmarks

import androidx.paging.*
import com.example.books.core.domain.bookmarks.CurrentUserBookmarksFlow
import com.example.books.core.model.Book
import com.example.books.feature.user.books.common.UserBookListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
  private val currentUserBookmarksFlow: com.example.books.core.domain.bookmarks.CurrentUserBookmarksFlow,
) : UserBookListViewModel() {

  override fun getBooksFlow(config: PagingConfig): Flow<PagingData<com.example.books.core.model.Book>> {
    return currentUserBookmarksFlow(config)
  }

}