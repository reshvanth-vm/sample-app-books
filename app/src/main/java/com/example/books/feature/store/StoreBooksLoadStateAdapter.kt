package com.example.books.feature.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.books.R

class StoreBooksLoadStateAdapter : LoadStateAdapter<StoreBooksLoadStateAdapter.ViewHolder>() {
  class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

  override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) = Unit

  override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
    return LayoutInflater
      .from(parent.context)
      .inflate(R.layout.item_store_books_load_state_loading, parent, false)
      .let(::ViewHolder)
  }

}