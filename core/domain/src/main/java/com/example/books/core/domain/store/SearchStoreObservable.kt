package com.example.books.core.domain.store

//
//class SearchStoreObservable @Inject constructor(
//  private val storeBooksSearchPagingDataFlow: StoreBooksSearchPagingDataFlow,
//  @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
//) {
//
//  fun flow(
//    query: Flow<CharSequence?>,
//    config: PagingConfig,
//    cacheScope: CoroutineScope,
//  ): Flow<StoreSearchState> {
//
//    return query
//      .onEach {
//        Log.e("search tag", "query $it")
//      }
////      .distinctUntilChanged { old, new ->
////        Log.e("search tag", "old $old and new $new")
////        old.toString() == new.toString()
////      }
//      .onEach {
//        Log.e("search tag", "query after distinct until cahgned $it")
//      }
//      .flatMapLatest { q ->
//        if (q.isNullOrBlank()) emptyStateFlow else storeBooksSearchPagingDataFlow
//          .storeBookCoversSearchPagingDataFlowMatching(q.toString(), config)
//          .cachedIn(cacheScope)
//          .mapLatest<PagingData<BookCover>, StoreSearchState>(StoreSearchState::SearchResult)
//      }.onStart {
//        emit(StoreSearchState.Searching)
////      delay(300)
//      }.flowOn(ioDispatcher)
//
//  }
//
//
//  companion object {
//    private val emptyStateFlow
//      get() = flowOf(StoreSearchState.SearchResult(PagingData.empty()))
//  }
//
//}
//
