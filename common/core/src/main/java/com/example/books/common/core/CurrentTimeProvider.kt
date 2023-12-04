package com.example.books.common.core

fun interface CurrentTimeProvider {
    fun getCurrentTime(): Long
}