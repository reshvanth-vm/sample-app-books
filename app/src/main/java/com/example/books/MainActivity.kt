package com.example.books

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.fragment.app.*
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.lifecycle.*
import com.example.books.databinding.ActivityMainBinding
import com.example.books.feature.bookmarks.BookmarksFragment
import com.example.books.feature.collection.CollectionsFragment
import com.example.books.feature.login.LoginFragment
import com.example.books.feature.store.StoreFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.example.books.R.id.frag_container_view as fragmentContainerViewId


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
                     NavigationBarView.OnItemSelectedListener,
                     NavigationBarView.OnItemReselectedListener {

  companion object {
    val TAG: String = MainActivity::class.java.simpleName
    val TopLvlDestTags = arrayOf(StoreFragment.TAG, CollectionsFragment.TAG)
  }

  private var _binding: ActivityMainBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    _binding = ActivityMainBinding.inflate(layoutInflater).also {
      setContentView(it.root)
      it.bottomNavView.apply {
        setOnItemSelectedListener(this@MainActivity)
        setOnItemReselectedListener(this@MainActivity)
      }
    }

    val viewModel by viewModels<MainViewModel>()

    if (savedInstanceState == null) {
      supportFragmentManager.apply {
        addFragmentOnAttachListener { _, fragment -> binding.onAttachNewFragment(fragment) }
        commit {
          setReorderingAllowed(true)
          val isLoggedIn = runBlocking(Dispatchers.IO) { viewModel.isUserLoggedInFlow.first() }
          binding.bottomNavView.isVisible = isLoggedIn
          onLoginInChanged(isLoggedIn)
        }
      }
    }

    var firstLoad = true
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.isUserLoggedInFlow.collectLatest { isLoggedIn ->
          binding.bottomNavView.isVisible = isLoggedIn
          if (firstLoad) {
            firstLoad = false
            return@collectLatest
          }
          with(supportFragmentManager) {
            val popTag = if (isLoggedIn) LoginFragment.TAG else binding.selectedTopNavTag
            popBackStack(popTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            commit {
              setReorderingAllowed(true)
              onLoginInChanged(isLoggedIn)
            }
          }
        }
      }
    }

  }

  override fun onDestroy() {
    _binding = null
    super.onDestroy()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val needPop = supportFragmentManager.fragments.lastOrNull()?.tag !in TopLvlDestTags

    val fragment = when (item.itemId) {
      R.id.store_menu_item      -> StoreFragment()
      R.id.collection_menu_item -> CollectionsFragment()
      R.id.bookmarks_menu_item  -> BookmarksFragment()
      else                      -> throw RuntimeException()
    }

    if (needPop) {
      supportFragmentManager.popBackStack(
        binding.selectedTopNavTag,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
      )
    }

    supportFragmentManager.fragments.lastOrNull()?.exitTransition = MaterialFadeThrough()
    fragment.enterTransition = MaterialFadeThrough()
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      replace(fragmentContainerViewId, fragment, item.tag)
    }

    return true
  }

  override fun onNavigationItemReselected(item: MenuItem) {
    supportFragmentManager.popBackStack(item.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
  }

  private val MenuItem.tag: String
    get() = getTopNavTagFromSelectedMenuId(itemId)

}

private val ActivityMainBinding.selectedTopNavTag
  get() = getTopNavTagFromSelectedMenuId(bottomNavView.selectedItemId)

private fun getTopNavTagFromSelectedMenuId(menuId: Int): String {
  return when (menuId) {
    R.id.store_menu_item      -> StoreFragment.TAG
    R.id.collection_menu_item -> CollectionsFragment.TAG
    R.id.bookmarks_menu_item  -> BookmarksFragment.TAG
    else                      -> throw RuntimeException()
  }
}


private fun ActivityMainBinding.onAttachNewFragment(fragment: Fragment) {
  val menuId = when (fragment) {
    is StoreFragment       -> R.id.store_menu_item
    is CollectionsFragment -> R.id.collection_menu_item
    is BookmarksFragment   -> R.id.bookmarks_menu_item
    else                   -> null
  }

//  Log.e(MainActivity.TAG, "fragment $fragment")
//  bottomNavView.isVisible = menuId != null
  bottomNavView.selectedItemId = menuId ?: return
}

private fun FragmentTransaction.onLoginInChanged(isLoggedIn: Boolean) {
//  val (fragmentClass, replaceTag) = if (isLoggedIn) {
//    StoreFragment::class to StoreFragment.TAG
//  } else {
//    LoginFragment::class to LoginFragment.TAG
//  }
  val (fragment, replaceTag) = if (isLoggedIn) {
    StoreFragment() to StoreFragment.TAG
  } else {
    LoginFragment() to LoginFragment.TAG
  }
  fragment.enterTransition = MaterialFadeThrough()
  replace(fragmentContainerViewId, fragment, replaceTag)
}