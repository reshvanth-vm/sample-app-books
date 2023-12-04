package com.example.books.core.data

import androidx.paging.*
import com.example.books.common.core.coroutine.*
import com.example.books.core.database.SampleBooksDatabase
import com.example.books.core.database.model.*
import com.example.books.core.model.*
import com.example.books.core.usecase.common.GetDetailedBook
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@Singleton
class BookRepoImpl @Inject constructor(
  private val db: SampleBooksDatabase,
  @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BookRepo,
    GetDetailedBook by db.bookDao() {

//  override fun storeBooksPagingDataFlow(pagingConfig: PagingConfig): Flow<PagingData<StoreBook>> {
//    return Pager(pagingConfig) { db.bookDao().storeBooksPagingSource() }.flow
//      .mapLatest<PagingData<StoreBookImpl>, PagingData<StoreBook>> { d -> d.map { it } }
//      .flowOn(ioDispatcher)
//  }

  override fun storeBookCoversPagingDataFlow(pagingConfig: PagingConfig): Flow<PagingData<BookCover>> {
    return Pager(pagingConfig) { db.bookDao().storeBookCoversPagingSource() }.flow
      .mapLatest<PagingData<BookCoverImpl>, PagingData<BookCover>> { d -> d.map { it } }
      .flowOn(ioDispatcher)
  }

  override fun collectionBooksFlow(userId: Long, config: PagingConfig): Flow<PagingData<Book>> {
    return Pager(config) { db.bookDao().collectionSource(userId) }.asBookData
  }

  override fun bookmarkedBooksFlow(userId: Long, config: PagingConfig): Flow<PagingData<Book>> {
    return Pager(config) { db.bookDao().bookmarksSource(userId) }.asBookData
  }

  override fun storeBookCoversSearchPagingDataFlowMatching(
    txt: String,
    config: PagingConfig,
  ): Flow<PagingData<BookCover>> {
    return Pager(config) { db.bookDao().storeSimpleBooksPagingSource(txt) }.flow
      .mapLatest<PagingData<BookCoverImpl>, PagingData<BookCover>> { d -> d.map { it } }
      .flowOn(ioDispatcher)
  }

  @OptIn(ExperimentalCoroutinesApi::class) private val Pager<Int, BookImpl>.asBookData
    get() = flow
      .mapLatest<PagingData<BookImpl>, PagingData<Book>> { d -> d.map { it } }
      .flowOn(ioDispatcher)

}