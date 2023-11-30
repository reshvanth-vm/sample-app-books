package com.example.books.base.binding.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.*
import androidx.databinding.BindingAdapter

@BindingAdapter(
  "android:updatePaddingBySystemBarInsets",
  "android:updateMarginBySystemBarInsets",
  requireAll = false,
)
fun View.updateInsets(
  updatePaddingBySystemBarInsets: Int = 0,
  updateMarginBySystemBarInsets: Int = 0,
) {

  ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, windowInsetsCompat ->

//    val windowInsetsCompat = ViewCompat.getRootWindowInsets(this) ?: return
    val systemBarInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())

    if (updatePaddingBySystemBarInsets != 0) {
      val viewInset = ViewInset(paddingStart, paddingTop, paddingEnd, paddingBottom)
      viewInset.obtainInsets(systemBarInsets, updatePaddingBySystemBarInsets)
      with(viewInset) {
        updatePadding(left, top, right, bottom)
      }
    }

    if (updateMarginBySystemBarInsets != 0) {
      val viewInset = ViewInset(marginStart, marginTop, marginEnd, marginBottom)
      viewInset.obtainInsets(systemBarInsets, updateMarginBySystemBarInsets)
      with(viewInset) {
        updateLayoutParams<MarginLayoutParams> {
          updateMargins(left, top, right, bottom)
        }
      }
    }

    windowInsetsCompat
  }

}

@BindingAdapter("visibility", requireAll = true)
fun View.visibility(isVisible: Boolean) {
  this.isVisible = isVisible
}