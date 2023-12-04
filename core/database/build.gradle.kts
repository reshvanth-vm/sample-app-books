plugins {
  alias(libs.plugins.books.core.android)
  alias(libs.plugins.books.android.room)
}

android {
  namespace = "com.example.books.core.database"
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
  arg("room.incremental", "true")
  arg("room.generateKotlin", "true")
}

dependencies {
  implementation(projects.core.model)
  implementation(projects.core.usecase)
}