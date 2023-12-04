package com.example.books.core.data

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