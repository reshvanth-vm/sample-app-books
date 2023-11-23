package com.example.books.core.domain.di

import com.example.books.core.domain.bookmarks.*
import com.example.books.core.domain.collection.*
import com.example.books.core.domain.profile.GetCurrentUser
import com.example.books.core.usecase.bookmark.*
import com.example.books.core.usecase.collection.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainActionsModule {

  @Provides
  fun provideABTCUC(
    user: GetCurrentUser,
    action: AddBooksToUserCollection,
  ) = AddBooksToCurrentUserCollection { action.addBooksToUserCollection(user().id, it) }

  @Provides
  fun provideABTCUB(
    user: GetCurrentUser,
    action: BookmarkBooks,
  ) = AddBooksToCurrentUserBookmarks { action.bookmarkBooks(user().id, it) }

  @Provides
  fun provideRBFCUC(
    user: GetCurrentUser,
    action: RemoveBooksFromUserCollection,
  ) = RemoveBooksFromCurrentUserCollection { action.removeBooksFromUserCollection(user().id, it) }

  @Provides
  fun provideRBFCUB(
    user: GetCurrentUser,
    action: RemoveBooksFromUserBookmarks,
  ) = RemoveBooksFromCurrentUserBookmarks { action.removeBooksFromUserBookmarks(user().id, it) }

}