package com.example.books.core.database.dao

import androidx.room.*
import com.example.books.core.database.dao.DaoProperty.ON_INSERT_CONFLICT_STRATEGY
import com.example.books.core.database.entity.*
import com.example.books.core.usecase.bookmark.IsUserBookmarkedBookFlow
import com.example.books.core.usecase.collection.IsUserCollectedBookFlow
import com.example.books.core.usecase.profile.GetRecentlyLoggedInUserEmails
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : IsUserCollectedBookFlow,
                    IsUserBookmarkedBookFlow,
                    GetRecentlyLoggedInUserEmails {

  @Insert(entity = UserEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
  suspend fun insertUsers(e: Collection<UserEntity>): LongArray

  @Insert(entity = UserBookEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
  suspend fun insertUserBooks(e: Collection<UserBookEntity>)

  @Insert(entity = UserBookmarkedBookEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
  suspend fun insertUserBookmarkedBooks(e: Collection<UserBookmarkedBookEntity>)

  @Delete(entity = UserBookEntity::class)
  suspend fun removeUserBooks(e: Collection<UserBookEntity>): Int

  @Delete(entity = UserBookmarkedBookEntity::class)
  suspend fun removeUserBookmarkedBooks(e: Collection<UserBookmarkedBookEntity>): Int

  @Query("DELETE FROM ${UserBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId IN (:bookIds)")
  suspend fun removeUserBooks(userId: Long, bookIds: Collection<Long>): Int

  @Query("DELETE FROM ${UserBookmarkedBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId IN (:bookIds)")
  suspend fun removeUserBookmarkedBooks(userId: Long, bookIds: Collection<Long>): Int

  @Query("SELECT EXISTS(SELECT * FROM ${UserEntity.TABLE_NAME} WHERE email = :email)")
  suspend fun isUserEmailExists(email: String): Boolean

  @Update
  suspend fun updateUser(user: UserEntity): Int

  @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE email = :email AND pwd = :pwd")
  suspend fun selectUser(email: String, pwd: String): UserEntity?

  @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE email = :email")
  suspend fun selectUser(email: String): UserEntity?

  @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE id = :id")
  suspend fun selectUser(id: Long): UserEntity?

  @Query("SELECT EXISTS(SELECT * FROM ${UserBookmarkedBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = :bookId LIMIT 1)")
  override fun isUserBookmarkedBookFlow(userId: Long, bookId: Long): Flow<Boolean>

  @Query("SELECT EXISTS(SELECT * FROM ${UserBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = :bookId LIMIT 1)")
  override fun isUserCollectedBookFlow(userId: Long, bookId: Long): Flow<Boolean>

  @Query("SELECT email FROM ${UserEntity.TABLE_NAME}")
  override suspend fun getRecentlyLoggedInUserEmail(): List<String>

  @Query("SELECT profileUri FROM ${UserEntity.TABLE_NAME} WHERE id = :userId")
  fun userAvatarFlow(userId: Long): Flow<String?>
}