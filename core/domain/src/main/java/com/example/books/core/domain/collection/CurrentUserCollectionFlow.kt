package com.example.books.core.domain.collection

import androidx.paging.*
import com.example.books.common.core.coroutine.*
import com.example.books.core.domain.profile.*
import com.example.books.core.usecase.collection.CollectionBooksFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrentUserCollectionFlow @Inject constructor(
  private val getUser: GetCurrentUser,
  private val getCollectionFlow: CollectionBooksFlow,
  @Dispatcher(AppDispatchers.IO) val dispatcher: CoroutineDispatcher,
) {
  operator fun invoke(config: PagingConfig): Flow<PagingData<com.example.books.core.model.Book>> {
    return getUser.flatMapFlow(
      onNull = flowOf(PagingData.empty()),
      onNotNull = { getCollectionFlow.collectionBooksFlow(id, config) }
    ).flowOn(dispatcher)
  }
}