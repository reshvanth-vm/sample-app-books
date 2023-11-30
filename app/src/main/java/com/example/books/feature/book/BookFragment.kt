package com.example.books.feature.book

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.contentValuesOf
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.core.view.insets.systemBarInsets
import androidx.fragment.app.*
import androidx.fragment.launchInLifecycleRepeatingScope
import com.example.books.R
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.databinding.FragmentBookBinding
import com.example.books.databinding.FragmentStoreBinding
import com.example.books.feature.common.BaseFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs


@AndroidEntryPoint
class BookFragment() : BaseFragment() {

  constructor(bookId: Long) : this() {
    arguments = bundleOf(ArgKey.BOOK_ID to bookId)
  }

  companion object {
    const val TAG = "book_screen"
  }

  object ArgKey {
    const val BOOK_ID = "book_id"
  }

  override val backStackTag: String = TAG

  private var _binding: FragmentBookBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
      drawingViewId = R.id.frag_container_view
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentBookBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val viewModel by viewModels<BookViewModel>()

    binding.apply {
      actionListener = viewModel
      toolBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
      backBtn?.setOnClickListener { parentFragmentManager.popBackStack() }

      if (root.isInPortraitMode) {
        appBar?.addOnOffsetChangedListener { _, i ->
          val collapsed = abs(i) > 600
          val color = if (collapsed) {
            MaterialColors.getColor(
              requireContext(),
              com.google.android.material.R.attr.colorOnSurface,
              Color.BLACK
            )
          } else {
            Color.WHITE
          }
          toolBar.setNavigationIconTint(color)
        }
      }
    }

    launchInLifecycleRepeatingScope {
      viewModel.uiState.collectLatest(binding::onUIStateChanged)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    val sysBarInsets = insets.systemBarInsets

    binding.toolBar.updateLayoutParams<MarginLayoutParams> {
      updateMargins(top = sysBarInsets.top)
    }

    binding.coverImgFilterView?.updateLayoutParams<MarginLayoutParams> {
      val left = if (sysBarInsets.left > 0) sysBarInsets.left else marginStart
      updateMargins(top = sysBarInsets.top, bottom = sysBarInsets.bottom, left = left)
    }

    return super.onApplyWindowInsets(v, insets)
  }

}

private fun FragmentBookBinding.onUIStateChanged(state: BookScreenState?) {
  val context = root.context

  val strokeColorAttr = com.google.android.material.R.attr.colorOutline
  val strokeColorStateList = MaterialColors.getColorStateListOrNull(context, strokeColorAttr)

  val bgColorAttr = com.google.android.material.R.attr.colorPrimary
  val bgColorStateList = MaterialColors.getColorStateListOrNull(context, bgColorAttr)

  val colorOnSurfaceAttr = com.google.android.material.R.attr.colorOnSurface
  val colorOnSurface = MaterialColors.getColor(context, colorOnSurfaceAttr, Color.BLACK)
  val colorOnSurfaceCSL = ColorStateList.valueOf(colorOnSurface)

  val colorSurfaceAttr = com.google.android.material.R.attr.colorSurface
  val colorSurface = MaterialColors.getColor(context, colorSurfaceAttr, Color.WHITE)

  val whiteCSL = ColorStateList.valueOf(Color.WHITE)

  state?.run {
    book = this
    bookmarkBtn.apply {
      if (isBookmarked) {
        setText(R.string.remove_from_bookmarks)
        setIconResource(R.drawable.ic_remove_from_bookmark)
        strokeWidth = resources.getDimension(R.dimen._1dp).toInt()
        strokeColor = strokeColorStateList
        backgroundTintList = ColorStateList(emptyArray(), intArrayOf(colorSurface))
        setTextColor(colorOnSurface)
        iconTint = colorOnSurfaceCSL
      } else {
        setText(R.string.bookmark)
        setIconResource(R.drawable.bookmark_filled_24px)
        backgroundTintList = bgColorStateList
        strokeWidth = 0
        setTextColor(Color.WHITE)
        iconTint = whiteCSL
      }
    }

    collectBtn.apply {
      if (isCollected) {
        setText(R.string.remove_from_collection)
        setIconResource(R.drawable.ic_remove_from_collection)
        strokeWidth = resources.getDimension(R.dimen._1dp).toInt()
        strokeColor = strokeColorStateList
        backgroundTintList = ColorStateList(emptyArray(), intArrayOf(colorSurface))
        setTextColor(colorOnSurface)
        iconTint = colorOnSurfaceCSL
      } else {
        setText(R.string.add_to_collection)
        setIconResource(R.drawable.ic_add_to_collection)
        backgroundTintList = bgColorStateList
        strokeWidth = 0
        setTextColor(Color.WHITE)
        iconTint = whiteCSL
      }
    }
//    bookmarkBtn.setText(if (isBookmarked) R.string.remove_from_bookmarks else R.string.bookmark)
//    collectBtn.setText(if (isCollected) R.string.remove_from_collection else R.string.add_to_collection)
  }
}
