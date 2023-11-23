package com.example.books.core.domain.collection

import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.*
import com.example.books.core.usecase.collection.IsUserCollectedBookFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class IsCurrentUserCollectedBookFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val isUserCollectedBookFlow: IsUserCollectedBookFlow,
  @Dispatcher(AppDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) {
  operator fun invoke(bookId: Long): Flow<Boolean> {
    return getUser.flatMapFlow(
      onNull = flowOf(false),
      onNotNull = { isUserCollectedBookFlow.isUserCollectedBookFlow(id, bookId) },
    ).flowOn(dispatcher)
  }
}