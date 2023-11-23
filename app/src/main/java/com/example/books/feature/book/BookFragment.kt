package com.example.books.feature.book

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.fragment.launchInLifecycleRepeatingScope
import com.example.books.R
import com.example.books.databinding.FragmentBookBinding
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookFragment() : Fragment(R.layout.fragment_book) {

  constructor(bookId: Long) : this() {
    arguments = bundleOf(ArgKey.BOOK_ID to bookId)
  }

  companion object {
    val TAG: String = BookFragment::class.java.simpleName
  }

  object ArgKey {
    const val BOOK_ID = "book_id"
  }

  //  private var _binding: FragmentBookBinding? = null
//  private val binding get() = _binding!!
  private val viewModel by viewModels<BookViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val containerViewId = R.id.frag_container_view
    val shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(resources.getDimension(R.dimen.book_cover_corner_radius))
    val (forwardDuration, reverseDuration) = arrayOf(
      R.integer.book_screen_enter_duration, R.integer.book_screen_pop_duration
    ).map { resources.getInteger(it).toLong() }

    sharedElementEnterTransition = MaterialContainerTransform().apply {
      drawingViewId = containerViewId
      duration = forwardDuration
//      startShapeAppearanceModel = shapeAppearanceModel
    }

    sharedElementReturnTransition = MaterialContainerTransform().apply {
      drawingViewId = containerViewId
      duration = reverseDuration
    }
  }

//  override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?,
//  ): View {
//    return if (_binding != null) binding else {
//      FragmentBookBinding.inflate(inflater, container, false).also {
//        _binding = it
//      }.apply {
//        actionListener = viewModel
//        backBtn.setOnClickListener { parentFragmentManager.popBackStack() }
//      }
//    }.root
//  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    FragmentBookBinding.bind(view).apply {
      actionListener = viewModel
      backBtn.setOnClickListener { parentFragmentManager.popBackStack() }

      launchInLifecycleRepeatingScope {
        with(viewModel) {
          launch { uiState.collectLatest(::onUIStateChanged) }
          launch { actionProcessState.collectLatest(::onActionProcessStateChanged) }
        }
      }
    }
  }

//  override fun onDestroyView() {
//    _binding = null
//    super.onDestroyView()
//  }

}

private fun FragmentBookBinding.onActionProcessStateChanged(process: BookScreenState.Action.Process) {
  return
  when (process) {

  }
}

private fun FragmentBookBinding.onUIStateChanged(state: BookScreenState?) {
  state?.run {
    book = this
//    positiveActionsGroup.isVisible = isCollected.not()
//    bookmarkBtn.isVisible = isCollected.not() && isBookmarked.not()
//    removeBtn.isVisible = isCollected

    bookmarkBtn.setText(if (isBookmarked) R.string.remove_from_bookmarks else R.string.bookmark)
    collectBtn.setText(if (isCollected) R.string.remove_from_collection else R.string.add_to_collection)
  }
}
