package com.example.books.core.datastore.serializer

import androidx.datastore.core.*
import com.example.books.core.datastore.*
import com.google.protobuf.InvalidProtocolBufferException
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

class UserCorePrefsSerializer @Inject constructor() : Serializer<UserCorePrefs> {

  override val defaultValue = userCorePrefs {
    loggedInUserId = -1
  }

  override suspend fun readFrom(input: InputStream): UserCorePrefs {
    return try {
      // readFrom is already called on the data store background thread
      UserCorePrefs.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto ${UserCorePrefs::class.simpleName}.", exception)
    }
  }

  override suspend fun writeTo(t: UserCorePrefs, output: OutputStream) {
    t.writeTo(output)
  }

}

