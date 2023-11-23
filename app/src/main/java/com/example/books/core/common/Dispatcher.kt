package com.example.books.core.common

import com.example.books.core.common.coroutine.AppDispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val niaDispatcher: AppDispatchers)