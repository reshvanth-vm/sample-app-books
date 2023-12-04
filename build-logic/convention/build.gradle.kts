import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "com.examples.books.build.logic"

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
  val srcDirNames = arrayOf("kotlin-ext", "util").map { "src/main/$it" }
  sourceSets["main"].java.srcDirs(srcDirNames)

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
//  compileOnly(libs.android.tools.common)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
  plugins {

    register("androidApplication") {
      id = "app.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }

    register("androidLibrary") {
      id = "app.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }

    register("androidHilt") {
      id = "app.android.hilt"
      implementationClass = "AndroidHiltConventionPlugin"
    }

    register("androidRoom") {
      id = "app.android.room"
      implementationClass = "AndroidRoomConventionPlugin"
    }

    register("jvmLibrary") {
      id = "app.jvm.library"
      implementationClass = "JvmLibraryConventionPlugin"
    }

    register("appCoreAndroid") {
      id = "app.core.android"
      implementationClass = "AppCoreAndroidConventionPlugin"
    }

    register("appFeatureAndroid") {
      id = "app.feature.android"
      implementationClass = "AppFeatureAndroidConventionPlugin"
    }
  }
}
