package com.example.books.feature.common

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.example.books.*
import com.example.books.feature.book.BookFragment
import com.example.books.feature.store.StoreFragment
import com.example.books.feature.user.books.bookmarks.BookmarksFragment
import com.example.books.feature.user.books.collection.CollectionsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.android.material.transition.*

// For now top lvl dest fragments are [StoreFragment, CollectionsFragment, BookmarksFragment]
abstract class TopLevelDestinationFragment : BaseFragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enterTransition = MaterialFadeThrough()
  }

  // todo remove this override function
  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)

    if (hidden.not()) {
      // for some reason :(
      // onApplying exit transition an unusual error* hits
      // error* - when navigating top lvl dest faster
      // with nav component (bottom nav view etc..)
      // white screen is placed and then view is not drawn
      exitTransition = null

      val navId = when (this) {
        is StoreFragment       -> R.id.store_menu_item
        is CollectionsFragment -> R.id.collection_menu_item
        is BookmarksFragment   -> R.id.bookmarks_menu_item
        else                   -> throw RuntimeException("top lvl dest is unknown $this*")
      }

      val activity = requireActivity() as MainActivity
      val navView = activity.run {
        val bottomNavView: BottomNavigationView? = findViewById(R.id.bottom_nav_view)
        val navRailView: NavigationRailView? = findViewById(R.id.nav_rail_view)
        bottomNavView ?: navRailView
      }

      navView?.selectedItemId = navId
    }
  }

}
