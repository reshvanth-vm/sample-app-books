package com.example.books.feature.store

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.*
import androidx.fragment.launchInLifecycleRepeatingScope
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.GridLayoutManager
import com.example.books.R
import com.example.books.core.model.*
import com.example.books.databinding.FragmentStoreBinding
import com.example.books.feature.book.BookFragment
import com.example.books.feature.profile.ProfileFragment
import com.example.books.feature.store.search.StoreSearchFragment
import com.google.android.material.transition.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment : Fragment(),
                      BookCoverPagingDataAdapter.Listener,
                      Toolbar.OnMenuItemClickListener,
                      StoreBookPagingDataAdapter.Listener,
                      StoreScreenEventHandler {

  companion object {
    val TAG: String = StoreFragment::class.java.simpleName
  }

  private var _binding: FragmentStoreBinding? = null
  private val binding get() = _binding!!
  private val storeBooksAdapter by lazy { BookCoverPagingDataAdapter(this) }
  private val userStoreBooksAdapter by lazy { StoreBookPagingDataAdapter(this) }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return if (_binding != null) binding else {
      FragmentStoreBinding
        .inflate(inflater, container, false)
        .also { _binding = it }
        .apply {
          toolBar.apply {
            setNavigationOnClickListener(::navigateToStoreSearchScreen)
            setOnMenuItemClickListener(this@StoreFragment)
          }
        }
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }

    binding.itemsRecyclerView.apply {
      layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//      adapter = storeBooksAdapter
      adapter = userStoreBooksAdapter
    }

    val viewModel by activityViewModels<StoreViewModel>()
    launchInLifecycleRepeatingScope {
//      launch { viewModel.books.collectLatest(storeBooksAdapter::submitData) }
//      launch { storeBooksAdapter.loadStateFlow.collectLatest(binding::onItemsLoadStates) }
      launch { viewModel.userBooks.collectLatest(userStoreBooksAdapter::submitData) }
      launch { userStoreBooksAdapter.loadStateFlow.collectLatest(binding::onItemsLoadStates) }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.profile_menu_item -> navigateToProfileScreen(item)
      else                   -> return false
    }
    return true
  }

  override fun navigateToStoreSearchScreen(view: View) {
    navigateToSearchOrProfileScreen(StoreSearchFragment(), StoreSearchFragment.TAG)
  }

  override fun navigateToProfileScreen(menuItem: MenuItem) {
    navigateToSearchOrProfileScreen(ProfileFragment(), ProfileFragment.TAG)
  }

  override fun navigateToBookDetailScreen(view: View, item: StoreBook) {
    navigateToBookDetailScreen(view, item as BookCover)
  }

  override fun navigateToBookDetailScreen(view: View, item: BookCover) {
    val (forwardDuration, reverseDuration) = resources.getIntArray(R.array.book_screen_container_transform_durations)
    exitTransition = MaterialElevationScale(false).setDuration(forwardDuration.toLong())
    reenterTransition = MaterialElevationScale(true).setDuration(reverseDuration.toLong())

    parentFragmentManager.commit {
      setReorderingAllowed(true)
      addSharedElement(view, resources.getString(R.string.book_screen_transition_name))
      replace(R.id.frag_container_view, BookFragment(item.id), BookFragment.TAG)
      addToBackStack(TAG)
    }
  }

}

private fun FragmentStoreBinding.onItemsLoadStates(states: CombinedLoadStates) {
  // todo
}

private fun StoreFragment.navigateToSearchOrProfileScreen(fragment: Fragment, tag: String) {
  exitTransition = MaterialFadeThrough()
  reenterTransition = MaterialFadeThrough()

  parentFragmentManager.commit {
    setReorderingAllowed(true)
    replace(R.id.frag_container_view, fragment, tag)
    addToBackStack(StoreFragment.TAG)
  }
}


//val Resources.storeBookListDetailInAndOutDuration
//  get() = arrayOf(
//    R.integer.store_book_list_detail_forward_motion_duration,
//    R.integer.store_book_list_detail_reverse_motion_duration
//  ).map { getInteger(it).toLong() }
//
//fun BookFragment.applySharedTransitions(
//  context: Context,
//  drawingViewId: Int,
//  forwardDuration: Long,
//  reverseDuration: Long,
//) = apply {
//  sharedElementEnterTransition = MaterialContainerTransform(context, true).apply {
//    this.drawingViewId = drawingViewId
//    duration = forwardDuration
//  }
//  sharedElementReturnTransition = MaterialContainerTransform(context, false).apply {
//    this.drawingViewId = drawingViewId
//    duration = reverseDuration
//  }
//}
//
//fun Fragment.applyTransitionsForBookItemDetailNavigation(
//  forwardDuration: Long,
//  reverseDuration: Long,
//) {
//  exitTransition = MaterialElevationScale(false).apply {
//    duration = forwardDuration
//  }
//
//  reenterTransition = MaterialElevationScale(true).apply {
//    duration = reverseDuration
//  }
//}
//
