package com.example.books.feature.user.books.collection

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.example.books.R
import com.example.books.feature.user.books.common.UserBookListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment : UserBookListFragment() {

  companion object {
    const val TAG = "user_collections_screen"
  }

  override val Context.title
    get() = resources.getString(R.string.collection)

  override val viewModel by activityViewModels<CollectionsViewModel>()

  override val backStackTag = TAG

}