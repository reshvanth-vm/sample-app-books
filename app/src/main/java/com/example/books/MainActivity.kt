package com.example.books

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.*
import androidx.core.view.insets.systemBarInsets
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.databinding.ActivityMainBinding
import com.example.books.feature.common.TopLevelDestinationFragment
import com.example.books.feature.store.StoreFragment
import com.example.books.feature.user.books.bookmarks.BookmarksFragment
import com.example.books.feature.user.books.collection.CollectionsFragment
import com.example.books.feature.user.login.LoginFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
                     FragmentOnAttachListener,
                     FragmentManager.OnBackStackChangedListener,
                     NavigationBarView.OnItemSelectedListener {

  companion object {
    const val TAG = "activity_main_screen"
    private const val NAV_ID_KEY = "top_lvl_nav_id_key"
    private const val NAV_VIEW_VISIBILITY_KEY = "is_nav_view_visible"
  }

  // members
  private var _binding: ActivityMainBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  private val navHelper = NavHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    supportFragmentManager.apply {
      addFragmentOnAttachListener(this@MainActivity)
      addOnBackStackChangedListener(this@MainActivity)
    }

    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.navView.apply {
      setOnItemSelectedListener(this@MainActivity)
      savedInstanceState?.apply {
        selectedItemId = getInt(NAV_ID_KEY)
        isVisible = getBoolean(NAV_VIEW_VISIBILITY_KEY)
      }
    }

    val viewModel by viewModels<MainViewModel>()

    val isFirstAppCreation = savedInstanceState == null
    var skipped = false

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.CREATED) {
        launch(Dispatchers.IO) {
          while (true) {
            val fragments = withContext(Dispatchers.Main) { supportFragmentManager.fragments }
            Log.e(TAG, "$fragments")
            delay(3000)
          }
        }

        launch {
          viewModel.isUserLoggedInFlow.collectLatest { isLoggedIn ->
            if (isFirstAppCreation.not() && skipped.not()) {
              skipped = true
              return@collectLatest
            }

            binding.navView.isVisible = isLoggedIn

            supportFragmentManager.commit {
              setReorderingAllowed(true)
              supportFragmentManager.fragments.forEach(::remove)

              if (isLoggedIn) {
                add(R.id.frag_container_view, StoreFragment(), StoreFragment.TAG)
              } else {
                add(R.id.frag_container_view, LoginFragment(), LoginFragment.TAG)
              }
            }
          }
        }
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(NAV_ID_KEY, binding.navView.selectedItemId)
    outState.putBoolean(NAV_VIEW_VISIBILITY_KEY, binding.navView.isVisible)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  override fun onBackStackChanged() {
    Log.e(TAG, "onBackStackChanged ${supportFragmentManager.fragments}")

    val navView = binding.navView

//    val toggleNavViewVisibility = isShowingAnyTopLvlDest != navView.isVisible
//    if (toggleNavViewVisibility) {
//      val (inset, updater) = binding.bottomNavView?.let {
//        sysBarInsets.bottom to { animatedValue: Int -> it.minimumHeight = animatedValue }
//      } ?: binding.navRailView?.let {
//        sysBarInsets.left to { animatedValue: Int -> it.minimumWidth = animatedValue }
//      } ?: throw RuntimeException()
//
//      Log.e(TAG, "inset $inset")
//      val mainVal = resources.getDimension(R.dimen.nav_view_size).toInt() + inset
//      val (from, to) = (if (isShowingAnyTopLvlDest) 0 to mainVal - inset else mainVal to 0)
//
//      Log.e(TAG, "from $from, to $to")
//      ValueAnimator.ofInt(from, to).setDuration(300).apply {
//        doOnStart {
//          if (isShowingAnyTopLvlDest) {
//            navView.isVisible = true
//          } else {
//            doOnEnd { navView.isVisible = false }
//          }
//        }
//
//        addUpdateListener { updater(it.animatedValue as Int) }
//      }.start()
//    }

    navView.isVisible = isAnyFragmentPresentOtherThanTopLvl.not()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    if (isAnyFragmentPresentOtherThanTopLvl) return true

    val navId = item.itemId
    val tag = navHelper.getFragmentTagByNavId(navId)

    val presentFragments = supportFragmentManager.fragments
    val currentTopLvlFragment = presentFragments.find { navHelper.isTagATopLvlFragmentTag(it.tag) && it.isHidden.not() }

    // navigating to the same top lvl navigation
    if (tag == currentTopLvlFragment?.tag) return true

    val neededFragmentInPresentFragments = presentFragments.find { it.tag == tag }

    Log.e(
      TAG, """
      ##############
      req tag $tag
      presentFrags $presentFragments
      curTopLvl $currentTopLvlFragment
      neededFRagINPresent $neededFragmentInPresentFragments
      ##############
    """.trimIndent()
    )

    supportFragmentManager.commit {
      setReorderingAllowed(true)
      currentTopLvlFragment?.let(::hide)

      if (neededFragmentInPresentFragments != null) {
        show(neededFragmentInPresentFragments)
      } else {
        val neededFragment = navHelper.getFragmentByTag(tag)
        add(R.id.frag_container_view, neededFragment, tag)
      }
    }

    return true
  }

  override fun onAttachFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    Log.e(TAG, "onAttachFragment $fragment")
    if (fragment.isHidden.not()) {
      val navId = navHelper.getFragmentNavIdByTag(fragment.tag)
      binding.navView.apply {
        if (navId != null && selectedItemId != navId) {
          selectedItemId = navId
        }
      }
    }
  }

}

private val ActivityMainBinding.navView
  get() = bottomNavView ?: navRailView ?: throw RuntimeException()


private object NavHelper {

  fun getFragmentTagByNavId(@IdRes navId: Int) = when (navId) {
    R.id.store_menu_item      -> StoreFragment.TAG
    R.id.collection_menu_item -> CollectionsFragment.TAG
    R.id.bookmarks_menu_item  -> BookmarksFragment.TAG
    else                      -> throw NotImplementedError("nav menuId '$navId' is unknown")
  }

  fun getFragmentByTag(tag: String): TopLevelDestinationFragment = when (tag) {
    StoreFragment.TAG       -> ::StoreFragment
    CollectionsFragment.TAG -> ::CollectionsFragment
    BookmarksFragment.TAG   -> ::BookmarksFragment
    else                    -> throw NotImplementedError("top lvl fragment tag '$tag' is unknown")
  }.invoke()

  fun getFragmentByNavId(@IdRes navId: Int): TopLevelDestinationFragment {
    val tag = getFragmentTagByNavId(navId)
    return getFragmentByTag(tag)
  }

  fun isTagATopLvlFragmentTag(tag: String?): Boolean {
    return tag in setOf(StoreFragment.TAG, CollectionsFragment.TAG, BookmarksFragment.TAG)
  }

  fun getFragmentNavIdByTag(tag: String?) = when (tag) {
    StoreFragment.TAG       -> R.id.store_menu_item
    CollectionsFragment.TAG -> R.id.collection_menu_item
    BookmarksFragment.TAG   -> R.id.bookmarks_menu_item
    else                    -> null
  }

}

private val MainActivity.isAnyFragmentPresentOtherThanTopLvl
  get() = supportFragmentManager.fragments.any {
    NavHelper.isTagATopLvlFragmentTag(it.tag).not()
  }

