package com.example.books.feature.store

import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.books.core.model.StoreBook
import com.example.books.databinding.ItemStoreBookBinding

class StoreBookPagingDataAdapter(
  private val listener: StoreBookPagingDataAdapter.Listener,
) : PagingDataAdapter<com.example.books.core.model.StoreBook, StoreBookPagingDataAdapter.ViewHolder>(DiffCallback) {

  interface Listener {
    fun navigateToBookDetailScreen(view: View, item: com.example.books.core.model.StoreBook)
  }

  class ViewHolder(val binding: ItemStoreBookBinding) : RecyclerView.ViewHolder(binding.root)

  object DiffCallback : DiffUtil.ItemCallback<com.example.books.core.model.StoreBook>() {
    override fun areItemsTheSame(oldItem: com.example.books.core.model.StoreBook, newItem: com.example.books.core.model.StoreBook): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: com.example.books.core.model.StoreBook, newItem: com.example.books.core.model.StoreBook): Boolean {
      return oldItem.isBookmarked == newItem.isBookmarked && oldItem.isCollected && newItem.isCollected
    }

  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position) ?: return
    holder.apply {
      binding.book = item
      itemView.setOnClickListener {
        listener.navigateToBookDetailScreen(it, item)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemStoreBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

}