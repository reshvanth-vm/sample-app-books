package com.example.books.feature.user.books.collection

import androidx.paging.*
import com.example.books.core.domain.collection.CurrentUserCollectionFlow
import com.example.books.core.model.Book
import com.example.books.feature.user.books.common.UserBookListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
  private val currentUserCollectionBooksFlow: CurrentUserCollectionFlow,
) : UserBookListViewModel() {

  override fun getBooksFlow(config: PagingConfig): Flow<PagingData<Book>> {
    return currentUserCollectionBooksFlow(config)
  }

}