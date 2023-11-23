package com.example.books.core.data.di

import com.example.books.core.data.*
import com.example.books.core.usecase.bookmark.IsUserBookmarkedBookFlow
import com.example.books.core.usecase.collection.IsUserCollectedBookFlow
import com.example.books.core.usecase.profile.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun bindBookRepo(repo: BookRepoImpl): BookRepo

  @Binds
  fun bindUserRepo(repo: UserRepoImpl): UserRepo

}