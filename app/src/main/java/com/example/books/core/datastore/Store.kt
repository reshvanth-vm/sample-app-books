package com.example.books.core.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.books.core.datastore.serializer.UserCorePrefsSerializer

val Context.userCorePrefsStore by dataStore(
  fileName = "user_core_prefs.pb",
  serializer = UserCorePrefsSerializer(),
)