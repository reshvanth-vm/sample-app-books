package com.example.books.core.common

import com.example.books.core.usecase.util.CurrentTimeProvider
import com.example.books.core.usecase.util.GlobalScopeProvider
import com.example.books.core.usecase.util.GlobalScopeRunnable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonUtils @Inject constructor() : CurrentTimeProvider,
                                          GlobalScopeRunnable,
                                          GlobalScopeProvider {

    override fun getCurrentTime(): Long {
        return Date().time
    }

    override fun runInGlobalScope(block: suspend () -> Unit) {
        getGlobalScope().launch {
            block()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun getGlobalScope(): CoroutineScope {
        return GlobalScope
    }

}