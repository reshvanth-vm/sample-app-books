package com.example.books.core.domain.collection

import androidx.paging.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.profile.*
import com.example.books.core.model.Book
import com.example.books.core.usecase.collection.CollectionBooksFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrentUserCollectionFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val getCollectionFlow: CollectionBooksFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {
  operator fun invoke(config: PagingConfig): Flow<PagingData<Book>> {
    return getUser.flatMapFlow(
      onNull = flowOf(PagingData.empty()),
      onNotNull = { getCollectionFlow.collectionBooksFlow(id, config) }
    ).flowOn(dispatcher)
  }
}