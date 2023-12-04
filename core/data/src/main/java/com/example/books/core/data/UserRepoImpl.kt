package com.example.books.core.data

import android.content.Context
import androidx.paging.*
import com.example.books.common.core.*
import com.example.books.common.core.coroutine.*
import com.example.books.core.database.SampleBooksDatabase
import com.example.books.core.database.dao.UserDao
import com.example.books.core.database.entity.*
import com.example.books.core.database.model.StoreBookImpl
import com.example.books.core.datastore.*
import com.example.books.core.model.*
import com.example.books.core.usecase.profile.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.security.MessageDigest
import javax.inject.*

@Singleton
class UserRepoImpl @Inject constructor(
  private val db: SampleBooksDatabase,
  currentTimeProvider: CurrentTimeProvider,
  globalScopeProvider: GlobalScopeProvider,
  @ApplicationContext context: Context,
  @ApplicationScope applicationScope: CoroutineScope,
  @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserRepo,
    UserDao by db.userDao(),
    CurrentTimeProvider by currentTimeProvider,
    GlobalScopeProvider by globalScopeProvider {

  private val prefs = context.userCorePrefsStore

  override val userFlow: Flow<User?> = prefs.data
    .mapLatest { selectUser(it.loggedInUserId) }
    .flowOn(ioDispatcher)
    .shareIn(applicationScope, SharingStarted.Eagerly, 1)

  override fun signInUser(user: User) {
    getGlobalScope().launch {
      prefs.updateData { it.copy { loggedInUserId = user.id } }
    }
  }

  override fun userAvatarFlow(): Flow<String?> {
    return prefs.data.flatMapLatest {
      if (it.loggedInUserId < 1) flowOf(null) else userAvatarFlow(it.loggedInUserId)
    }
  }

  override fun currentUserFlow(): Flow<User> {
    return userFlow.filterNotNull()
  }

  override suspend fun authenticateUser(
    email: CharSequence?,
    pwd: CharSequence?,
  ): UserAuthenticator.Result {
    var e: User? = null
    val invalidReason = when {
      email.isNullOrBlank() -> UserAuthenticator.InvalidReason.BLANK_EMAIL
      pwd.isNullOrBlank()   -> UserAuthenticator.InvalidReason.BLANK_PWD
      else                  -> {
        e = selectUser(email.toString())
        when {
          e == null             -> UserAuthenticator.InvalidReason.EMAIL_NOT_FOUND
          e.pwd != pwd.toHash() -> UserAuthenticator.InvalidReason.PWD_NOT_MATCHED
          else                  -> null
        }
      }
    }

    val result = invalidReason?.let(UserAuthenticator.Result::Error) ?: e?.let(UserAuthenticator.Result::Success)
    return result ?: throw RuntimeException("no user found and also no invalid reason is throwed")
  }

  override suspend fun signUp(credentials: NewUserCredentials): User {
    val entity = with(credentials) {
      UserEntity(id = 0, profileUri = profileUri, name = name, email = email, pwd = pwd.toHash())
    }
    val id = withContext(ioDispatcher) { insertUsers(setOf(entity)).first() }
    return entity.copy(id = id)
  }

  override fun addBooksToUserCollection(userId: Long, bookIds: Collection<Long>) {
    val currentTime = getCurrentTime()
    val books = bookIds.map { UserBookEntity(userId, it, currentTime) }
    runDataChanges { insertUserBooks(books) }
  }

  override fun bookmarkBooks(userId: Long, bookIds: Collection<Long>) {
    val currentTime = getCurrentTime()
    val books = bookIds.map { UserBookmarkedBookEntity(userId, it, currentTime) }
    runDataChanges { insertUserBookmarkedBooks(books) }
  }

  override fun userStoreBooksPagingDataFlow(pagingConfig: PagingConfig): Flow<PagingData<StoreBook>> {
    return prefs.data.mapLatest { it.loggedInUserId }.flatMapLatest { userId ->
      if (userId < 1) flowOf(PagingData.empty()) else {
        Pager(pagingConfig) {
          db.bookDao().storeBooksPagingSource(userId)
        }.flow.mapLatest<PagingData<StoreBookImpl>, PagingData<StoreBook>> { d -> d.map { it } }
      }
    }.flowOn(ioDispatcher)
  }

  override fun removeBooksFromUserCollection(userId: Long, bookIds: Collection<Long>) {
    runDataChanges { removeUserBooks(userId, bookIds) }
  }

  override fun removeBooksFromUserBookmarks(userId: Long, bookIds: Collection<Long>) {
    runDataChanges { removeUserBookmarkedBooks(userId, bookIds) }
  }

  override suspend fun validateUserEmail(email: CharSequence?): NewUserEmailValidator.InvalidReason? {
    if (email.isNullOrBlank()) return NewUserEmailValidator.InvalidReason.BLANK
    val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(email)
    if (matcher.matches().not()) return NewUserEmailValidator.InvalidReason.ERROR
    if (isUserEmailExists(email = email.toString())) return NewUserEmailValidator.InvalidReason.EXIST
    return null
  }

  override fun signOutUser() {
    getGlobalScope().launch {
      prefs.updateData { it.copy { loggedInUserId = -1 } }
    }
  }

  override suspend fun validateNewPwd(pwd: CharSequence?): NewUserPwdValidator.InvalidReason? {
    if (pwd.isNullOrBlank()) return NewUserPwdValidator.InvalidReason.BLANK
//    if (pwd.length < 4) return NewUserPwdValidator.InvalidReason.SHORT
//    if (pwd.length > 8) return NewUserPwdValidator.InvalidReason.LONG
    val isValid = setOf(
      Char::isDigit,
      Char::isUpperCase,
      Char::isLowerCase
    ).all { pwd.count(it) == 2 }

    val specials = pwd.count { it in "`~!@#$%^&*()_-+={}[]\\|'\";:,<.>/?" } == 2
    if (isValid.not() || specials.not()) return NewUserPwdValidator.InvalidReason.NOT_MATCHED_CONDITION

    return null
  }

  private fun runDataChanges(block: suspend CoroutineScope.() -> Unit) {
    getGlobalScope().launch(ioDispatcher, block = block)
  }

}

private fun CharSequence.toHash(): String {
  return toMD5()
}

private fun CharSequence.toMD5(): String {
  val bytes = MessageDigest.getInstance("MD5").digest(this.toString().toByteArray())
  return bytes.toHex()
}

private fun ByteArray.toHex(): String {
  return joinToString("") { "%02x".format(it) }
}