plugins {
  alias(libs.plugins.books.core.android)
  alias(libs.plugins.books.android.room)
}

android {
  namespace = "com.example.books.core.data"
}

dependencies {
  implementation(projects.core.model)
  implementation(projects.core.usecase)
  implementation(projects.core.database)
  implementation(projects.core.datastore)

  implementation(libs.androidx.dataStore.core)
  implementation(libs.protobuf.kotlin.lite)
}
