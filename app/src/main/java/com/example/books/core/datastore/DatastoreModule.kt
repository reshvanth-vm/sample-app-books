package com.example.books.core.datastore

//import android.content.Context
//import androidx.datastore.core.*
//import androidx.datastore.dataStoreFile
//import com.example.books.core.common.Dispatcher
//import com.example.books.core.common.coroutine.*
//import com.example.books.core.datastore.serializer.UserCorePrefsSerializer
//import dagger.*
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.*
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatastoreModule {
//
//  @Provides
//  @Singleton
//  fun providesUserCorePrefsStore(
//    @ApplicationContext context: Context,
//    @Dispatcher(AppDispatchers.IO) dispatcher: CoroutineDispatcher,
//    @ApplicationScope scope: CoroutineScope,
//    serializer: UserCorePrefsSerializer,
//  ): DataStore<UserCorePrefs> {
//    return DataStoreFactory.create(
//      serializer = serializer,
//      scope = CoroutineScope(scope.coroutineContext + dispatcher),
//    ) { context.dataStoreFile("user_core_prefs.pb") }
//  }
//
////  @Provides
////  @Singleton
////  fun providesUserFeaturePrefsStore(
////    @ApplicationContext context: Context,
////  ): DataStore<UserFeaturePrefs> {
////    return context.userFeaturePrefsStore
////  }
//
//}