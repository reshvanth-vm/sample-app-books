package com.example.books.feature.book

import androidx.lifecycle.*
import com.example.books.core.common.Dispatcher
import com.example.books.core.common.coroutine.AppDispatchers
import com.example.books.core.domain.bookmarks.*
import com.example.books.core.domain.collection.*
import com.example.books.core.model.DetailedBook
import com.example.books.core.usecase.common.GetDetailedBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
  ssh: SavedStateHandle,
  bookProvider: GetDetailedBook,
  isCurrentUserBookmarkedBookFlow: IsCurrentUserBookmarkedBookFlow,
  isCurrentUserCollectedBookFlow: IsCurrentUserCollectedBookFlow,
  private val collectContract: AddBooksToCurrentUserCollection,
  private val bookmarkContract: AddBooksToCurrentUserBookmarks,
  private val removeFromCollectionContract: RemoveBooksFromCurrentUserCollection,
  private val removeFromBookmarksContract: RemoveBooksFromCurrentUserBookmarks,
  @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel(),
    BookScreenState.Action.Listener {

  private data class ActionsState(
    val isBookmarked: Boolean,
    val isCollected: Boolean,
  ) {
    companion object {
      val Default = ActionsState(isCollected = false, isBookmarked = false)
    }
  }

  override val onAction: (BookScreenState.Action) -> Unit

  val uiState: Flow<BookScreenState>
  val actionProcessState: StateFlow<BookScreenState.Action.Process> //  get() = _actionProcess.asStateFlow()

  init {
    actionProcessState = MutableStateFlow<BookScreenState.Action.Process>(BookScreenState.Action.Process.Loading)

    val bookId = ssh.get<Long>(BookFragment.ArgKey.BOOK_ID) ?: throw RuntimeException()
    val book = runBlocking(ioDispatcher) { bookProvider.getDetailedBook(bookId) }

    val actions = combine(
      isCurrentUserBookmarkedBookFlow(bookId),
      isCurrentUserCollectedBookFlow(bookId),
      ::ActionsState,
    ).onEach {
      actionProcessState.value = BookScreenState.Action.Process.None
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.Eagerly, ActionsState.Default)

    uiState = actions.mapLatest {
      BookScreenState(dBook = book, isBookmarked = it.isBookmarked, isCollected = it.isCollected)
    }

//    uiState =  combine(
//          isCurrentUserBookmarkedBookFlow(bookId),
//          isCurrentUserCollectedBookFlow(bookId),
//          ::Pair,
//        ).filter { bookFetchJob.isCompleted }.onEach {
//          actionProcessState.value = BookScreenState.Action.Process.None
//        }.distinctUntilChanged().mapLatest { (bookmarked, collected) ->
//          BookScreenState(dBook = book, isBookmarked = bookmarked, isCollected = collected)
//        }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    onAction = { action ->
      val bookIds = setOf(bookId)
      when (action) {
//        BookScreenState.Action.Bookmark             -> {
//          actionProcessState.value = BookScreenState.Action.Process.Bookmarking
//          bookmarkContract.bookmarkBooks(bookIds)
//        }
//        BookScreenState.Action.Collect              -> {
//          actionProcessState.value = BookScreenState.Action.Process.Collecting
//          collectContract.addBooksToCurrentUserCollection(bookIds)
//        }
//        BookScreenState.Action.RemoveFromCollection -> {
//          actionProcessState.value = BookScreenState.Action.Process.RemovingFromCollection
//          removeFromCollectionContract.removeBooksFromUserCollection(bookIds)
//        }
        BookScreenState.Action.ToggleBookmark -> {
          // todo update action state process
          if (actions.value.isBookmarked) {
            removeFromBookmarksContract.removeBooksFromCurrentUserBookmarks(bookIds)
          } else {
            bookmarkContract.bookmarkBooks(bookIds)
          }
        }

        BookScreenState.Action.ToggleCollect  -> {
          if (actions.value.isCollected) {
            removeFromCollectionContract.removeBooksFromUserCollection(bookIds)
          } else {
            collectContract.addBooksToCurrentUserCollection(bookIds)
          }
        }
      }
    }
  }

//  companion object {
//    val book = object : DetailedBook {
//      override val pagesCount: Int
//        get() = 100
//      override val description: String
//        get() = "ih the"
//      override val publisher: String
//        get() = "publsi"
//      override val publishedDate: String
//        get() = "data"
//      override val rating: Float
//        get() = 4.5f
//      override val ratingCount: Int
//        get() = 1231
//      override val name: String
//        get() = "name"
//      override val author: String
//        get() = "authro"
//      override val id: Long
//        get() = Long.MAX_VALUE
//      override val coverUrl: String
//        get() = ""
//
//    }
//  }

}


// # tried first getting book
//    lateinit var book: DetailedBook
//    val bookFetchJob = viewModelScope.launch(ioDispatcher) {
//      book = bookProvider.getDetailedBook(bookId)
//    }

//     # tried blocking and getting book
// # trying flow
//    val book = flow {
//      emit(bookProvider.getDetailedBook(bookId))
//    }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

//      .mapLatest { (bookmarked, collected) ->
//      BookScreenState(dBook = book, isBookmarked = bookmarked, isCollected = collected)
//    }

//    uiState = combine(book, actions, ::Pair).mapLatest { (b, ac) ->
//      BookScreenState(dBook = b, isBookmarked = ac.first, isCollected = ac.second)
//    }

