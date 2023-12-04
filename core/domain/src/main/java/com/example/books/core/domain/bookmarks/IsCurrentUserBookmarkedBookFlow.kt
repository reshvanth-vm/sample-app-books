package com.example.books.core.domain.bookmarks

import com.example.books.common.core.coroutine.*
import com.example.books.core.domain.profile.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class IsCurrentUserBookmarkedBookFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val isUserBookmarkedBookFlow: com.example.books.core.usecase.IsUserBookmarkedBookFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {
  operator fun invoke(bookId: Long): Flow<Boolean> {
    return getUser.flatMapFlow(
      onNull = flowOf(false),
      onNotNull = { isUserBookmarkedBookFlow.isUserBookmarkedBookFlow(id, bookId) },
    ).flowOn(dispatcher)
  }
}
