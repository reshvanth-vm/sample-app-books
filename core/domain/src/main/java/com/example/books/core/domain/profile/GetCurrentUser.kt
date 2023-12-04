package com.example.books.core.domain.profile

import com.example.books.common.core.coroutine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
  private val repo: com.example.books.core.data.UserRepo,
  @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {

  operator fun invoke() = runBlocking(ioDispatcher) {
    repo.userFlow.first()!!
  }

  fun flow() = repo.userFlow

}

fun <T> GetCurrentUser.flatMapFlow(onNull: Flow<T>, onNotNull: com.example.books.core.model.User.() -> Flow<T>): Flow<T> {
  return flow().flatMapLatest {
    if (it == null) onNull else onNotNull(it)
  }
}

