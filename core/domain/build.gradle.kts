plugins {
  alias(libs.plugins.books.core.android)
}

android {
  namespace = "com.example.books.core.domain"
}

dependencies {
  implementation(projects.core.data)
  implementation(projects.core.model)
  implementation(projects.core.usecase)
}