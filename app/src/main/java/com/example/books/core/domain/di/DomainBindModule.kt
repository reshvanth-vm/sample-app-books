package com.example.books.core.domain.di

import com.example.books.core.data.*
import com.example.books.core.usecase.bookmark.*
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
  fun bindGetDetailedBook(repo: BookRepo): GetDetailedBook

  @Binds
  fun bindStoreBooksSource(repo: BookRepo): StoreBooksPagingDataFlow

  @Binds
  fun bindUSBPDF(repo: UserRepo): UserStoreBooksPagingDataFlow

  @Binds
  fun bindABTC(repo: UserRepo): AddBooksToUserCollection

  @Binds
  fun bindBB(repo: UserRepo): BookmarkBooks

  @Binds
  fun bindRBFC(repo: UserRepo): RemoveBooksFromUserCollection

  @Binds
  fun bindRBFUB(repo: UserRepo): RemoveBooksFromUserBookmarks

  @Binds
  fun bindCBF(repo: BookRepo): CollectionBooksFlow

  @Binds
  fun bindBBF(repo: BookRepo): BookmarkedBooksFlow

  @Binds
  fun bindIUBBF(repo: UserRepo): IsUserBookmarkedBookFlow

  @Binds
  fun bindIUCBF(repo: UserRepo): IsUserCollectedBookFlow

  @Binds
  fun bindUA(repo: UserRepo): UserAuthenticator

  @Binds
  fun bindSUU(repo: UserRepo): SignUpUser

  @Binds
  fun bindSIU(repo: UserRepo): SignInUser

  @Binds
  fun bindSOU(repo: UserRepo): SignOutUser

  @Binds
  fun bindNUEV(repo: UserRepo): NewUserEmailValidator

  @Binds
  fun bindNUPV(repo: UserRepo): NewUserPwdValidator

  @Binds
  fun bindGRULE(repo: UserRepo): GetRecentlyLoggedInUserEmails

  @Binds
  fun bindSBSPDF(repo: BookRepo): StoreBooksSearchPagingDataFlow

  @Binds
  fun bindCUF(repo: UserRepo): CurrentUserFlow

  @Binds
  fun bindUAF(repo: UserRepo): UserAvatarFlow
}