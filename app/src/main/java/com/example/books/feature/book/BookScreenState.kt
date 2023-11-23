package com.example.books.feature.book

import com.example.books.core.model.DetailedBook

data class BookScreenState(
  private val dBook: DetailedBook,
  val isBookmarked: Boolean,
  val isCollected: Boolean,
) : DetailedBook by dBook {

  constructor(dBook: DetailedBook) : this(dBook, false, false)

  sealed interface Action {
    //    data object Collect : Action
//    data object Bookmark : Action
//    data object RemoveFromCollection : Action
    data object ToggleCollect : Action
    data object ToggleBookmark : Action

    interface Listener {
      //      fun onAction(action: Action)
      val onAction: (Action) -> Unit
    }

    sealed interface Process {
      data object Collecting : Process
      data object Bookmarking : Process
      data object RemovingFromCollection : Process
      data object RemovingFromBookmarks : Process
      data object None : Process
      data object Loading : Process
    }
  }

}
