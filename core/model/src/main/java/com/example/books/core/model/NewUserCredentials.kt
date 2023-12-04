package com.example.books.core.model

interface NewUserCredentials {
  val name: String
  val email: String
  val pwd: String
  val profileUri: String?
}