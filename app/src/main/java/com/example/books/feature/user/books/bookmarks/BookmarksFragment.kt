package com.example.books.feature.user.books.bookmarks

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.example.books.R
import com.example.books.feature.user.books.common.UserBookListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : UserBookListFragment() {

  companion object {
    const val TAG = "user_bookmarks_screen"
  }

  override val Context.title
    get() = resources.getString(R.string.bookmarks)

  override val viewModel by activityViewModels<BookmarksViewModel>()

  override val backStackTag = TAG

}