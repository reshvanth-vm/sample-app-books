plugins {
  alias(libs.plugins.books.core.android)
}

android {
  namespace = "com.examples.books.core.usecase"
}

dependencies {
  implementation(projects.core.model)
}