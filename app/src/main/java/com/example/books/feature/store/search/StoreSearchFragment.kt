package com.example.books.feature.store.search

import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.core.view.*
import androidx.fragment.*
import androidx.fragment.app.*
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.example.books.R
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.core.model.BookCover
import com.example.books.databinding.*
import com.example.books.feature.book.BookFragment
import com.example.books.feature.common.BaseFragment
import com.example.books.feature.store.*
import com.google.android.material.transition.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreSearchFragment : BaseFragment(),
                            StoreSearchScreenStateEventListener,
                            BookCoverPagingDataAdapter.Listener {
  companion object {
    const val TAG = "store_search_screen"
  }

  override val backStackTag = TAG

  private var _binding: FragmentStoreSearchBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  private val resultsAdapter by lazy { BookCoverPagingDataAdapter(this) }
  private val viewModel by viewModels<StoreSearchViewModel>()

  private var isKeyboardVisible = false

//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    enterTransition = MaterialFadeThrough()
////    exitTransition = MaterialFadeThrough()
//  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentStoreSearchBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {

      listener = this@StoreSearchFragment
      queryTxtInputLayout.setStartIconOnClickListener { parentFragmentManager.popBackStack() }
      showKeyboard(queryInputEditTxt)

      resultsRecyclerView.apply {
        adapter = resultsAdapter
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
          override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (isKeyboardVisible) hideKeyboard(queryInputEditTxt)
          }
        })
      }
    }

    launchInLifecycleRepeatingScope {
      binding.run {
        with(viewModel) {
          launch { queryInputEditTxt.setText(currentSearchQuery) }
          launch { searchFlow.collectLatest(resultsAdapter::submitData) }
          launch { screenState.collectLatest(::onScreenState) }
          launch { resultsAdapter.loadStateFlow.collectLatest(::onLoadStates) }
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
    return super.onApplyWindowInsets(v, insets)
  }

  override fun afterSearchQueryTextChanged(text: Editable?) {
    val query = text?.toString()
    viewModel.currentSearchQuery = query
    binding.msgTxtView.apply {
      isVisible = query.isNullOrBlank()
      setText(R.string.store_search_init_msg)
    }
  }

  override fun navigateToBookDetailScreen(view: View, item: com.example.books.core.model.BookCover) {
    if (isKeyboardVisible) {
      hideKeyboard(binding.queryInputEditTxt)
    }

    parentFragmentManager.commit {
      setReorderingAllowed(true)
      addSharedElement(view, resources.getString(R.string.book_screen_transition_name))
      hide(this@StoreSearchFragment)
      add(R.id.frag_container_view, BookFragment(item.id), BookFragment.TAG)
      addToBackStack(TAG)
    }
  }

  private fun onLoadStates(combinedLoadStates: CombinedLoadStates) {
    val isRefreshing = combinedLoadStates.refresh is LoadState.Loading

    val haveItemsInAdapter = resultsAdapter.itemCount > 0
    val isResultsEmpty = combinedLoadStates.append.endOfPaginationReached && haveItemsInAdapter.not()

    val isQueryEmpty = binding.queryInputEditTxt.text?.toString().isNullOrBlank()

    binding.msgTxtView.apply {
      isVisible = isQueryEmpty || isResultsEmpty
      val msgRes = if (isResultsEmpty) R.string.store_search_no_results_msg else R.string.store_search_init_msg
      setText(msgRes)
    }
  }

}

private fun FragmentStoreSearchBinding.onScreenState(state: StoreSearchScreenState) {
  var isSearching = false
  when (state) {
    StoreSearchScreenState.Init        -> Unit
    // unfortunately we can only get whether the results are empty by combinedLoadStates
    StoreSearchScreenState.EmptySearch -> Unit
    StoreSearchScreenState.Searching   -> isSearching = true
  }

  loadingCircularProgressIndicator.isVisible = isSearching
  resultsRecyclerView.isVisible = isSearching.not()

}


//  private suspend fun onStoreSearchState(state: StoreSearchState) {
//    var isSearching = state is StoreSearchState.Searching
//    Log.e(TAG, "searchState $state")
//
//    binding.apply {
//      loadingCircularProgressIndicator.isVisible = isSearching
//      resultsRecyclerView.isVisible = isSearching.not()
//    }
//
//    when (state) {
//      StoreSearchState.Searching       -> isSearching = true
//      is StoreSearchState.SearchResult -> resultsAdapter.submitData(state.result)
//    }
//