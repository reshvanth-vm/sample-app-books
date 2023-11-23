package com.example.books.core.usecase.util

fun interface GlobalScopeRunnable {
    fun runInGlobalScope(block: suspend () -> Unit)
}

