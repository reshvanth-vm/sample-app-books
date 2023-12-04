package com.example.books.core.domain.di

import com.example.books.core.usecase.collection.*
import com.example.books.core.usecase.common.GetDetailedBook
import com.example.books.core.usecase.profile.*
import com.example.books.core.usecase.store.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainBindModule {

  @Binds
  fun bindGetDetailedBook(repo: com.example.books.core.data.BookRepo): GetDetailedBook

  @Binds
  fun bindStoreBooksSource(repo: com.example.books.core.data.BookRepo): StoreBooksPagingDataFlow

  @Binds
  fun bindUSBPDF(repo: com.example.books.core.data.UserRepo): UserStoreBooksPagingDataFlow

  @Binds
  fun bindABTC(repo: com.example.books.core.data.UserRepo): AddBooksToUserCollection

  @Binds
  fun bindBB(repo: com.example.books.core.data.UserRepo): com.example.books.core.usecase.BookmarkBooks

  @Binds
  fun bindRBFC(repo: com.example.books.core.data.UserRepo): RemoveBooksFromUserCollection

  @Binds
  fun bindRBFUB(repo: com.example.books.core.data.UserRepo): com.example.books.core.usecase.RemoveBooksFromUserBookmarks

  @Binds
  fun bindCBF(repo: com.example.books.core.data.BookRepo): CollectionBooksFlow

  @Binds
  fun bindBBF(repo: com.example.books.core.data.BookRepo): com.example.books.core.usecase.BookmarkedBooksFlow

  @Binds
  fun bindIUBBF(repo: com.example.books.core.data.UserRepo): com.example.books.core.usecase.IsUserBookmarkedBookFlow

  @Binds
  fun bindIUCBF(repo: com.example.books.core.data.UserRepo): IsUserCollectedBookFlow

  @Binds
  fun bindUA(repo: com.example.books.core.data.UserRepo): UserAuthenticator

  @Binds
  fun bindSUU(repo: com.example.books.core.data.UserRepo): SignUpUser

  @Binds
  fun bindSIU(repo: com.example.books.core.data.UserRepo): SignInUser

  @Binds
  fun bindSOU(repo: com.example.books.core.data.UserRepo): SignOutUser

  @Binds
  fun bindNUEV(repo: com.example.books.core.data.UserRepo): NewUserEmailValidator

  @Binds
  fun bindNUPV(repo: com.example.books.core.data.UserRepo): NewUserPwdValidator

  @Binds
  fun bindGRULE(repo: com.example.books.core.data.UserRepo): GetRecentlyLoggedInUserEmails

  @Binds
  fun bindSBSPDF(repo: com.example.books.core.data.BookRepo): StoreBooksSearchPagingDataFlow

  @Binds
  fun bindCUF(repo: com.example.books.core.data.UserRepo): CurrentUserFlow

  @Binds
  fun bindUAF(repo: com.example.books.core.data.UserRepo): UserAvatarFlow
}