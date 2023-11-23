package com.example.books.core.domain.bookmarks

import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.*
import com.example.books.core.model.User
import com.example.books.core.usecase.bookmark.IsUserBookmarkedBookFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class IsCurrentUserBookmarkedBookFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val isUserBookmarkedBookFlow: IsUserBookmarkedBookFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {
  operator fun invoke(bookId: Long): Flow<Boolean> {
    return getUser.flatMapFlow(
      onNull = flowOf(false),
      onNotNull = { isUserBookmarkedBookFlow.isUserBookmarkedBookFlow(id, bookId) },
    ).flowOn(dispatcher)
  }
}
