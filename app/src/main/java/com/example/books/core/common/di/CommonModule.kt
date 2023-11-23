package com.example.books.core.common.di

import com.example.books.core.common.CommonUtils
import com.example.books.core.usecase.util.CurrentTimeProvider
import com.example.books.core.usecase.util.GlobalScopeProvider
import com.example.books.core.usecase.util.GlobalScopeRunnable
import dagger.Binds
import dagger.Module
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