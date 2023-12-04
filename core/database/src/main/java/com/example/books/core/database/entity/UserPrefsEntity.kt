package com.example.books.core.database.entity

import androidx.room.*

@Entity(
  tableName = UserPrefsEntity.TABLE_NAME,
  foreignKeys = [ForeignKey(
    entity = UserEntity::class,
    parentColumns = ["id"],
    childColumns = ["userId"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
  )],
)
data class UserPrefsEntity(
  @PrimaryKey val userId: Long,
  val theme: Int,
) {
  companion object {
    const val TABLE_NAME = "user_prefs"
  }
}