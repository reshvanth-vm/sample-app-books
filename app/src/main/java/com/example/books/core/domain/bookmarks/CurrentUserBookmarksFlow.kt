package com.example.books.core.domain.bookmarks

import androidx.paging.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.*
import com.example.books.core.model.Book
import com.example.books.core.usecase.bookmark.BookmarkedBooksFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrentUserBookmarksFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val getBookmarksFlow: BookmarkedBooksFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {

  operator fun invoke(config: PagingConfig): Flow<PagingData<Book>> {
    return getUser.flatMapFlow(
      onNull = flowOf(PagingData.empty()),
      onNotNull = { getBookmarksFlow.bookmarkedBooksFlow(id, config) }
    ).flowOn(dispatcher)
  }
}
