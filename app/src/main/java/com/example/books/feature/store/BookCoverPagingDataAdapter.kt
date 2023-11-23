package com.example.books.feature.store

import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.books.R
import com.example.books.core.model.BookCover
import com.example.books.databinding.ItemGridBookCoverBinding

class BookCoverPagingDataAdapter(
  private val listener: Listener,
) : PagingDataAdapter<BookCover, BookCoverPagingDataAdapter.ViewHolder>(DiffCallback) {

  fun interface Listener {
    fun navigateToBookDetailScreen(view: View, item: BookCover)
  }

  class ViewHolder(val binding: ItemGridBookCoverBinding) : RecyclerView.ViewHolder(binding.root) {
    constructor(view: View) : this(ItemGridBookCoverBinding.bind(view))
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
    return LayoutInflater
      .from(parent.context)
      .inflate(R.layout.item_grid_book_cover, parent, false)
      .let(::ViewHolder)
  }

  private object DiffCallback : DiffUtil.ItemCallback<BookCover>() {
    override fun areItemsTheSame(oldItem: BookCover, newItem: BookCover): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookCover, newItem: BookCover): Boolean {
      return true
    }

  }
}


//class StoreBooksPagingAdapter(
//  private val listener: SimpleBookActionsListener,
//) : PagingDataAdapter<SimpleBook, StoreBooksPagingAdapter.ViewHolder>(DiffCallback) {
//
//  fun interface SimpleBookActionsListener {
//    fun onInvokeStoreBook(binding: ItemGridSimpleBookBinding, item: SimpleBook)
//  }
//
//  class ViewHolder(val binding: ItemGridSimpleBookBinding) : RecyclerView.ViewHolder(binding.root) {
//    constructor(view: View) : this(ItemGridSimpleBookBinding.bind(view))
//  }
//
//  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    val item = getItem(position) ?: return
//    holder.apply {
//      binding.book = item
//      itemView.setOnClickListener {
//        listener.onInvokeStoreBook(binding, item)
//      }
//    }
//  }
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//    return LayoutInflater
//      .from(parent.context)
//      .inflate(R.layout.item_grid_simple_book, parent, false)
//      .let(::ViewHolder)
//  }
//
//  private object DiffCallback : DiffUtil.ItemCallback<SimpleBook>() {
//    override fun areItemsTheSame(oldItem: SimpleBook, newItem: SimpleBook): Boolean {
//      return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: SimpleBook, newItem: SimpleBook): Boolean {
//      return true
//    }
//  }
//}
//
