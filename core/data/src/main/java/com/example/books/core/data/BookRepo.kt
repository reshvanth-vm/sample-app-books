package com.example.books.core.data

import com.example.books.core.usecase.BookmarkedBooksFlow
import com.example.books.core.usecase.collection.CollectionBooksFlow
import com.example.books.core.usecase.common.GetDetailedBook
import com.example.books.core.usecase.store.*

interface BookRepo : StoreBooksPagingDataFlow,
                     GetDetailedBook,
                     CollectionBooksFlow,
                     BookmarkedBooksFlow,
                     StoreBooksSearchPagingDataFlow

