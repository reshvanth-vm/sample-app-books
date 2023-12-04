package com.example.books.common.core.di

import com.example.books.common.core.*
import com.example.books.common.core.coroutine.CommonUtils
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CommonModule {

  @Binds
  fun bindCurrentTimeProvider(utils: CommonUtils): CurrentTimeProvider

  @Binds
  fun bindGlobalScopeRunnable(utils: CommonUtils): GlobalScopeRunnable

  @Binds
  fun bindGlobalScopeProvider(utils: CommonUtils): GlobalScopeProvider

}