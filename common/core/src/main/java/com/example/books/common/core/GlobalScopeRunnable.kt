package com.example.books.common.core

fun interface GlobalScopeRunnable {
    fun runInGlobalScope(block: suspend () -> Unit)
}

