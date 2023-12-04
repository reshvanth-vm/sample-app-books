package com.example.books.core.model

interface User {
  val id: Long
  val name: String
  val email: String
  val profileUri: String?
}