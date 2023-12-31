package com.example.books.feature.user.profile.parcel

import android.os.Parcelable
import com.example.books.core.model.NewUserCredentials
import kotlinx.parcelize.*

@Parcelize
data class NewUserCredentialsParcel(
  override val name: String,
  override val email: String,
  override val pwd: String,
  override val profileUri: String?,
) : com.example.books.core.model.NewUserCredentials,
    Parcelable {
  companion object {
    val KEY: String = NewUserCredentialsParcel::class.java.simpleName
  }
}
