package com.example.books.core.database.model

import com.example.books.core.model.BookCover

data class BookCoverImpl(override val id: Long, override val coverUrl: String) : BookCover

