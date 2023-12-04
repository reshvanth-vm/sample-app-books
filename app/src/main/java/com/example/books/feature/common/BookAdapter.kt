package com.example.books.feature.common

import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.books.core.model.Book
import com.example.books.databinding.ItemListBookBinding

class BookAdapter(
  private val listener: Listener,
) : PagingDataAdapter<com.example.books.core.model.Book, BookAdapter.ViewHolder>(BookDiffCallback) {

  fun interface Listener {
    fun navigateToBookDetailScreen(binding: ItemListBookBinding, item: com.example.books.core.model.Book)
  }

  class ViewHolder(val binding: ItemListBookBinding) : RecyclerView.ViewHolder(binding.root)

  object BookDiffCallback : DiffUtil.ItemCallback<com.example.books.core.model.Book>() {
    override fun areItemsTheSame(oldItem: com.example.books.core.model.Book, newItem: com.example.books.core.model.Book): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: com.example.books.core.model.Book, newItem: com.example.books.core.model.Book): Boolean {
      return true
    }

  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position) ?: return
    holder.apply {
      binding.book = item
      itemView.setOnClickListener {
        listener.navigateToBookDetailScreen(binding, item)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ItemListBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      .let(BookAdapter::ViewHolder)
  }
}