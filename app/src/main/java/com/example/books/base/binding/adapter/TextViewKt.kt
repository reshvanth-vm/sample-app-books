package com.example.books.base.binding.adapter

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("android:desperateText")
fun TextView.bindSetDesperateText(desperateText: CharSequence?) {
  text = desperateText
  isVisible = text.isNullOrBlank().not()
}