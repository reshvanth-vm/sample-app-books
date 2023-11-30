package com.example.books.feature.common.base.fragment

import android.view.View
import androidx.fragment.app.commit
import com.example.books.R
import com.example.books.feature.book.BookFragment
import com.example.books.feature.common.TopLevelDestinationFragment
import com.google.android.material.transition.MaterialElevationScale


fun TopLevelDestinationFragment.navigateToBookDetailScreen(view: View, bookId: Long) {
  exitTransition =  MaterialElevationScale(false)
  reenterTransition = MaterialElevationScale(true)

  val bookFragmentTransitionName = resources.getString(R.string.book_screen_transition_name)

  parentFragmentManager.commit {
    setReorderingAllowed(true)
    addSharedElement(view, bookFragmentTransitionName)
    hide(this@navigateToBookDetailScreen)
    add(R.id.frag_container_view, BookFragment(bookId), BookFragment.TAG)
    addToBackStack(backStackTag)
  }
}
