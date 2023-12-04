package com.example.books.common.core.coroutine

import com.example.books.common.core.*
import kotlinx.coroutines.*
import java.util.Date
import javax.inject.*

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