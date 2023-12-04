plugins {
  alias(libs.plugins.books.android.application)
  alias(libs.plugins.books.android.hilt)
  id("kotlin-parcelize")
  kotlin("kapt")
}

android {
  namespace = "com.example.books"

  sourceSets["main"].java.srcDirs("src/main/kotlin-ext")
}

kapt {
  generateStubs = true
  correctErrorTypes = true
}

dependencies {
  implementation(projects.common.core)
  implementation(projects.common.feature)

  implementation(projects.core.model)
  implementation(projects.core.domain)
  implementation(projects.core.usecase)


  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)

  implementation(libs.coil)

  implementation(libs.androidx.fragment.ktx)

  implementation(libs.androidx.paging.runtime.ktx)

  implementation(libs.androidx.dataStore.core)
  implementation(libs.protobuf.kotlin.lite)
  implementation(libs.kotlin.reflect)
}
