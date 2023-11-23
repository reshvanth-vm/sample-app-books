package com.example.books.core.datastore.serializer

import androidx.datastore.core.*
import com.example.books.core.datastore.*
import com.google.protobuf.InvalidProtocolBufferException
import java.io.*
import javax.inject.Inject

class UserFeaturePrefsSerializer @Inject constructor() : Serializer<UserFeaturePrefs> {

  override val defaultValue = userFeaturePrefs {
    theme = 1
  }

  override suspend fun readFrom(input: InputStream): UserFeaturePrefs {
    return try {
      // readFrom is already called on the data store background thread
      UserFeaturePrefs.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto ${UserCorePrefs::class.simpleName}.", exception)
    }
  }

  override suspend fun writeTo(t: UserFeaturePrefs, output: OutputStream) {
    t.writeTo(output)
  }

}