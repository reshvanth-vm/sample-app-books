package com.example.books.core.database.entity

import androidx.room.*
import com.example.books.core.model.User

@Entity(tableName = UserEntity.TABLE_NAME, indices = [Index("email", unique = true)])
data class UserEntity(
  @PrimaryKey(autoGenerate = true) override val id: Long,
  override val profileUri: String?,
  override val name: String,
  override val email: String,
  val pwd: String,
//    val acctCreationDate: Instant,
//    val lastLoginDate: Instant,
) : User {
  companion object {
    const val TABLE_NAME = "user"
  }
}