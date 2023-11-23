package com.example.books.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

  @Singleton
  @Provides
  fun providesDB(@ApplicationContext context: Context): SampleBooksDatabase {
    return Room
      .databaseBuilder(context, SampleBooksDatabase::class.java, SampleBooksDatabase.FILE_NAME)
      .createFromAsset("database/sample_books.db")
      .build()
  }

}
