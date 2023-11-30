package com.example.books.feature.user.books.common

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.*
import androidx.core.view.insets.systemBarInsets
import androidx.fragment.app.commit
import androidx.fragment.launchInLifecycleRepeatingScope
import androidx.paging.*
import com.example.books.R
import com.example.books.base.exception.*
import com.example.books.core.model.Book
import com.example.books.databinding.*
import com.example.books.feature.common.*
import com.example.books.feature.common.base.fragment.navigateToBookDetailScreen
import com.example.books.feature.store.StoreFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class UserBookListFragment : TopLevelDestinationFragment(),
                                      BookAdapter.Listener {

  protected abstract val Context.title: String

  protected abstract val viewModel: UserBookListViewModel

  protected open val bookAdapter by lazy { BookAdapter(this) }

  private var _binding: FragmentUserBookListBinding? = null
  protected val binding: FragmentUserBookListBinding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentUserBookListBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      toolBar.title = requireContext().title
      itemsRecyclerView.adapter = bookAdapter
      goStoreBtn.setOnClickListener(::goToStore)
    }

    launchInLifecycleRepeatingScope {
      launch { viewModel.booksFlow.collectLatest(bookAdapter::submitData) }
      launch { bookAdapter.loadStateFlow.collectLatest { binding.onCombinedLoadStates(it) } }
    }
  }

  open fun FragmentUserBookListBinding.onCombinedLoadStates(states: CombinedLoadStates) {
    val isRefreshing = states.refresh is LoadState.Loading
    val isLoadingMore = states.append is LoadState.Loading
    val isEmpty = isRefreshing.not() && states.append.endOfPaginationReached && bookAdapter.itemCount < 1

    refreshCircularProgressIndicator.isVisible = isRefreshing
    emptyMsg.isVisible = isEmpty
    goStoreBtn.isVisible = isEmpty
    itemsRecyclerView.isVisible = isEmpty.not()

    val indicatorSize = root.context.resources.getDimension(R.dimen.collection_loading_more_indicator_size)
    itemsRecyclerView.translationY = if (isLoadingMore) -indicatorSize else 0f
    loadingMoreCircularProgressIndicator.isVisible = isLoadingMore
  }

  final override fun navigateToBookDetailScreen(binding: ItemListBookBinding, item: Book) {
    navigateToBookDetailScreen(binding.root, item.id)
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    val sysBarInsets = insets.systemBarInsets

    binding.apply {
      itemsRecyclerView.updatePadding(right = sysBarInsets.right, bottom = sysBarInsets.bottom)
      toolBar.updateLayoutParams<MarginLayoutParams> {
        updateMargins(left = sysBarInsets.left, top = sysBarInsets.top, right = sysBarInsets.right)
      }
    }

    return super.onApplyWindowInsets(v, insets)
  }

}

private fun UserBookListFragment.goToStore(btn: View) {
  val presentFragments = parentFragmentManager.fragments
  val storeFragment = presentFragments.find { it.tag == StoreFragment.TAG } ?: throw TopLvlDestinationNotPresentException()

  parentFragmentManager.commit {
    setReorderingAllowed(true)
    hide(this@goToStore)
    show(storeFragment)
//    replace<StoreFragment>(R.id.frag_container_view, StoreFragment.TAG)
  }
}

