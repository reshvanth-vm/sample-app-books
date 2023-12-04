package com.example.books.feature.store

import android.os.Bundle
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.core.view.insets.systemBarInsets
import androidx.fragment.app.*
import androidx.fragment.launchInLifecycleRepeatingScope
import com.example.books.R
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.databinding.FragmentStoreBinding
import com.example.books.feature.common.TopLevelDestinationFragment
import com.example.books.feature.common.base.fragment.navigateToBookDetailScreen
import com.example.books.feature.store.search.StoreSearchFragment
import com.example.books.feature.user.profile.ProfileFragment
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class StoreFragment : TopLevelDestinationFragment(),
                      Toolbar.OnMenuItemClickListener,
                      StoreBookPagingDataAdapter.Listener {

  companion object {
    const val TAG = "store_screen"
  }

  override val backStackTag = TAG

  private var _binding: FragmentStoreBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  private val userStoreBooksAdapter by lazy {
    StoreBookPagingDataAdapter(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentStoreBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      itemsRecyclerView.adapter = userStoreBooksAdapter.withLoadStateHeaderAndFooter(
        header = StoreBooksLoadStateAdapter(),
        footer = StoreBooksLoadStateAdapter(),
      )

      toolBar.apply {
        setOnMenuItemClickListener(this@StoreFragment)
        setNavigationOnClickListener {
          navigateToSearchOrProfileScreen(StoreSearchFragment(), StoreSearchFragment.TAG)
        }
      }
    }

    val viewModel by activityViewModels<StoreViewModel>()
    launchInLifecycleRepeatingScope {
      viewModel.userBooks.collectLatest(userStoreBooksAdapter::submitData)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.profile_menu_item) {
      navigateToSearchOrProfileScreen(ProfileFragment(), ProfileFragment.TAG)
    }

    return true
  }

  override fun navigateToBookDetailScreen(view: View, item: com.example.books.core.model.StoreBook) {
    navigateToBookDetailScreen(view, item.id)
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    val sysBarInsets = insets.systemBarInsets

    binding.toolBar.updateLayoutParams<MarginLayoutParams> {
      updateMargins(top = sysBarInsets.top)
    }

    return super.onApplyWindowInsets(v, insets)
  }

}

private fun StoreFragment.navigateToSearchOrProfileScreen(fragment: Fragment, tag: String) {
  reenterTransition = MaterialFadeThrough()

  parentFragmentManager.commit {
    setReorderingAllowed(true)
    hide(this@navigateToSearchOrProfileScreen)
    add(R.id.frag_container_view, fragment, tag)
    addToBackStack(StoreFragment.TAG)
  }
}