package com.example.books.core.usecase.profile

fun interface GetRecentlyLoggedInUserEmails {
  suspend fun getRecentlyLoggedInUserEmail(): List<String>
}

