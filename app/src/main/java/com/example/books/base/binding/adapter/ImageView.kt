package com.example.books.base.binding.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.*
import coil.request.ImageRequest
import coil.size.Scale
import com.example.books.R

@BindingAdapter("coilLoad") fun ImageView.coilLoad(data: Any?) {
  directLoad(data)
  // buildAndLoad(data)
}

private fun ImageView.buildAndLoad(data: Any?) {
  ImageRequest.Builder(context).data(data).applyDefaults().listener { _, res ->
      setImageDrawable(res.drawable)
    }.build().let(context.imageLoader::enqueue)
}

private fun ImageView.directLoad(data: Any?) {
  load(data) {
    applyDefaults()
  }
}

private fun ImageRequest.Builder.applyDefaults(): ImageRequest.Builder {
  return this
    .crossfade(true)
    .crossfade(60)
    .placeholder(R.drawable.sample_book_cover)
    .scale(Scale.FILL)
}