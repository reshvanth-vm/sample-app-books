package com.example.books.core.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.books.core.database.dao.DaoProperty.ON_INSERT_CONFLICT_STRATEGY
import com.example.books.core.database.entity.*
import com.example.books.core.database.model.*
import com.example.books.core.usecase.common.GetDetailedBook

@Dao
interface BookDao : GetDetailedBook {

  @Insert(entity = BookEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
  suspend fun insertBooks(e: Collection<BookEntity>): LongArray

//    @Insert(entity = BookAuthorEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
//    suspend fun insertAuthors(e: Collection<BookAuthorEntity>): LongArray
//
//    @Insert(entity = BookAuthorXRefEntity::class, onConflict = ON_INSERT_CONFLICT_STRATEGY)
//    suspend fun insertBookAuthorsXRef(e: Collection<BookAuthorXRefEntity>)

  @RewriteQueriesToDropUnusedColumns
  @Query("SELECT * FROM ${BookEntity.TABLE_NAME} ORDER BY ratingCount DESC, rating DESC")
  fun storeSimpleBooksPagingSource(): PagingSource<Int, SimpleBookImpl>

  @Query("SELECT id,coverUrl FROM ${BookEntity.TABLE_NAME} ORDER BY ratingCount DESC, rating DESC")
  fun storeBookCoversPagingSource(): PagingSource<Int, BookCoverImpl>

  @RewriteQueriesToDropUnusedColumns
  @Query("SELECT * FROM ${BookEntity.TABLE_NAME} WHERE NAME LIKE '%' || :matching || '%' ORDER BY ratingCount DESC, rating DESC")
  fun storeSimpleBooksPagingSource(matching: String): PagingSource<Int, BookCoverImpl>

  @RewriteQueriesToDropUnusedColumns
  @Query(
    "SELECT *, " +
    "EXISTS(SELECT * FROM ${UserBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = id LIMIT 1) AS isCollected, " +
    "EXISTS(SELECT * FROM ${UserBookmarkedBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = id LIMIT 1) AS isBookmarked " +
    "FROM ${BookEntity.TABLE_NAME} ORDER BY ratingCount DESC, rating DESC"
  )
  fun storeBooksPagingSource(userId: Long): PagingSource<Int, StoreBookImpl>

//  @RewriteQueriesToDropUnusedColumns
//  @Query(
//    "SELECT *, " +
//    "EXISTS(SELECT * FROM ${UserBookEntity.TABLE_NAME} WHERE bookId = id LIMIT 1) AS isCollected, " +
//    "EXISTS(SELECT * FROM ${UserBookmarkedBookEntity.TABLE_NAME} WHERE bookId = id LIMIT 1) AS isBookmarked " +
//    "FROM ${BookEntity.TABLE_NAME} ORDER BY ratingCount DESC, rating DESC LIMIT 3"
//  )
//  fun storeBooksPagingSourceTemp(): List<StoreBookImpl>

//  @RewriteQueriesToDropUnusedColumns
//  @Query("SELECT * FROM ${BookEntity.TABLE_NAME} WHERE NAME LIKE '%' || :matching || '%' ORDER BY ratingCount DESC, rating DESC")
//  fun storeBooksPagingSource(matching: String): PagingSource<Int, StoreBookImpl>

  @RewriteQueriesToDropUnusedColumns
  @Query("SELECT * FROM ${UserBookEntity.TABLE_NAME} JOIN ${BookEntity.TABLE_NAME} ON id = bookId WHERE userId = :userId ORDER BY ratingCount DESC, rating DESC")
  fun collectionSource(userId: Long): PagingSource<Int, BookImpl>

  @RewriteQueriesToDropUnusedColumns
  @Query("SELECT * FROM ${UserBookmarkedBookEntity.TABLE_NAME} JOIN ${BookEntity.TABLE_NAME} ON id = bookId WHERE userId = :userId ORDER BY ratingCount DESC, rating DESC")
  fun bookmarksSource(userId: Long): PagingSource<Int, BookImpl>

  //    @Query("SELECT b.*, EXISTS(SELECT * FROM ${UserBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = :bookId) AS isInUserCollection, EXISTS(SELECT * FROM ${UserBookmarkedBookEntity.TABLE_NAME} WHERE userId = :userId AND bookId = :bookId) AS isBookmarked FROM ${BookEntity.TABLE_NAME} AS b WHERE id = :bookId")
  @Query("SELECT * FROM ${BookEntity.TABLE_NAME} WHERE id = :bookId")
  suspend fun getBook(bookId: Long): BookEntity

  override suspend fun getDetailedBook(bookId: Long): com.example.books.core.model.DetailedBook {
    return getBook(bookId)
  }
}

//val s = """SELECT *, EXISTS(SELECT * FROM user_book WHERE bookId = id LIMIT 1) AS isCollected, EXISTS(SELECT * FROM user_bookmarked_book WHERE bookId = id LIMIT 1) AS isBookmarked FROM book ORDER BY ratingCount DESC, rating DESC LIMIT 3"""
