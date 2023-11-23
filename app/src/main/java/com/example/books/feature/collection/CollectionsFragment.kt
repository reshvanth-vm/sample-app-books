package com.example.books.feature.collection

import android.content.*
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.*
import androidx.fragment.app.*
import androidx.paging.*
import com.example.books.R
import com.example.books.core.model.Book
import com.example.books.databinding.*
import com.example.books.feature.book.BookFragment
import com.example.books.feature.common.BookAdapter
import com.example.books.feature.store.StoreFragment
import com.google.android.material.transition.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment : Fragment(),
                            BookAdapter.Listener,
                            CollectionScreenActionListener {

  companion object {
    val TAG: String = CollectionsFragment::class.java.simpleName
  }

  private val collectionAdapter = BookAdapter(this)

  private var _binding: FragmentCollectionsBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return if (_binding != null) binding else {
      FragmentCollectionsBinding
        .inflate(inflater, container, false)
        .also { _binding = it }
        .apply { listener = this@CollectionsFragment }
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }

    binding.itemsRecyclerView.adapter = collectionAdapter

    val viewModel by activityViewModels<CollectionsViewModel>()
    launchInLifecycleRepeatingScope {
      launch { viewModel.books.collectLatest(collectionAdapter::submitData) }
      launch { collectionAdapter.loadStateFlow.collectLatest(::onCombinedLoadStates) }
    }
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  override fun navigateToBookDetailScreen(binding: ItemListBookBinding, item: Book) {
    val bookFragmentTransitionName = resources.getString(R.string.book_screen_transition_name)
    val bookFragment = BookFragment()
    bookFragment.arguments = bundleOf(BookFragment.ArgKey.BOOK_ID to item.id)
    val (forwardDuration, reverseDuration) = resources.collectionBookListDetailInAndOutDuration

    applyTransitionsForItemDetailNavigation(forwardDuration, reverseDuration)
    bookFragment.applySharedTransitions(
      requireContext(),
      R.id.frag_container_view,
      forwardDuration,
      reverseDuration
    )

    parentFragmentManager.commit {
      setReorderingAllowed(true)
      addSharedElement(binding.root, bookFragmentTransitionName)
      replace(R.id.frag_container_view, bookFragment, BookFragment.TAG)
      addToBackStack(TAG)
    }
  }

  override fun goToStore(btn: View) {
    parentFragmentManager.commit {
      setReorderingAllowed(true)
      replace<StoreFragment>(R.id.frag_container_view, StoreFragment.TAG)
    }
  }

  private fun onCombinedLoadStates(states: CombinedLoadStates) {
    val isRefreshing = states.refresh is LoadState.Loading
    val isLoadingMore = states.append is LoadState.Loading
    val isEmpty = isRefreshing.not() && states.append.endOfPaginationReached && collectionAdapter.itemCount < 1

    binding.apply {
      refreshCircularProgressIndicator.isVisible = isRefreshing
      emptyMsg.isVisible = isEmpty
      goStoreBtn.isVisible = isEmpty
      itemsRecyclerView.isVisible = isEmpty.not()

      val indicatorSize = root.context.resources.getDimension(R.dimen.collection_loading_more_indicator_size)
      itemsRecyclerView.translationY = if (isLoadingMore) -indicatorSize else 0f
      loadingMoreCircularProgressIndicator.isVisible = isLoadingMore
    }
  }
}

private val Resources.collectionBookListDetailInAndOutDuration
  get() = arrayOf(
    R.integer.collection_book_list_detail_forward_motion_duration,
    R.integer.collection_book_list_detail_reverse_motion_duration
  ).map { getInteger(it).toLong() }

private fun BookFragment.applySharedTransitions(
  context: Context,
  drawingViewId: Int,
  forwardDuration: Long,
  reverseDuration: Long,
) = apply {
  val scrimColor = Color.TRANSPARENT
  val colorSurface = context.surfaceColor
  sharedElementEnterTransition = MaterialContainerTransform(context, true).apply {
    this.drawingViewId = drawingViewId
//    setAllContainerColors(colorSurface)
    duration = forwardDuration
//    interpolator = PathInterpolatorCompat.create(0.3f, 0f, 0.8f, 0.15f)
//    this.scrimColor = scrimColor
  }
  sharedElementReturnTransition = MaterialContainerTransform(context, false).apply {
    this.drawingViewId = drawingViewId
//    interpolator = PathInterpolatorCompat.create(.05f, .7f, .1f, 1f)
//    setAllContainerColors(context.surfaceColor)
//    this.scrimColor = scrimColor
    duration = reverseDuration
  }
}

private fun CollectionsFragment.applyTransitionsForItemDetailNavigation(
  forwardDuration: Long,
  reverseDuration: Long,
) {
  exitTransition = MaterialElevationScale(false).apply {
    duration = forwardDuration
  }

  reenterTransition = MaterialElevationScale(true).apply {
    duration = reverseDuration
  }
}

