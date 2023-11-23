package com.example.books.core.usecase.util

fun interface CurrentTimeProvider {
    fun getCurrentTime(): Long
}