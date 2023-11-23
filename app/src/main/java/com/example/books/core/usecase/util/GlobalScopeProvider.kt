package com.example.books.core.usecase.util

import kotlinx.coroutines.CoroutineScope

fun interface GlobalScopeProvider {
    fun getGlobalScope(): CoroutineScope
}