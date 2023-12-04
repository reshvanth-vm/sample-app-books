package com.example.books.common.core

import kotlinx.coroutines.CoroutineScope

fun interface GlobalScopeProvider {
    fun getGlobalScope(): CoroutineScope
}