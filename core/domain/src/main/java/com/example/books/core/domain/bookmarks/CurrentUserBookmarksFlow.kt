package com.example.books.core.domain.bookmarks

import androidx.paging.*
import com.example.books.common.core.coroutine.*
import com.example.books.core.domain.profile.*
import com.example.books.core.model.Book
import com.example.books.core.usecase.BookmarkedBooksFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrentUserBookmarksFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val getBookmarksFlow: com.example.books.core.usecase.BookmarkedBooksFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {

  operator fun invoke(config: PagingConfig): Flow<PagingData<com.example.books.core.model.Book>> {
    return getUser.flatMapFlow(
      onNull = flowOf(PagingData.empty()),
      onNotNull = { getBookmarksFlow.bookmarkedBooksFlow(id, config) }
    ).flowOn(dispatcher)
  }
}
