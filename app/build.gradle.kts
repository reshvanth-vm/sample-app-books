import com.android.build.api.variant.VariantSelector
import dagger.hilt.android.plugin.util.capitalize
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.protobuf)
  kotlin("kapt")
  id("kotlin-parcelize")
}

android {
  namespace = "com.example.books"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.example.books"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    sourceSets["main"].java {
      val dirs = arrayOf("kotlin-ext")
      srcDir(dirs.map { "src/main/$it" })
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

kapt {
  generateStubs = true
  correctErrorTypes = true
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
  arg("room.incremental", "true")
  arg("room.generateKotlin", "true")
}

hilt {
  enableAggregatingTask = false
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
//    val warningsAsErrors: String? by project
//    allWarningsAsErrors = warningsAsErrors.toBoolean()
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-opt-in=kotlin.RequiresOptIn",
      // Enable experimental coroutines APIs, including Flow
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlinx.coroutines.FlowPreview",
    )
  }
}

protobuf {
  protoc {
    artifact = libs.protobuf.protoc.get().toString()
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        register("java") {
          option("lite")
        }
        register("kotlin") {
          option("lite")
        }
      }
    }
  }
}

dependencies {

//  implementation(project(":core:datastore"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  implementation(libs.androidx.room.runtime)
  annotationProcessor(libs.androidx.room.compiler)
  ksp(libs.androidx.room.room.compiler)
  implementation(libs.androidx.room.paging)
  implementation(libs.androidx.room.ktx)

//    implementation(libs.kotlinx.datetime)

  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  ksp(libs.dagger.compiler) // Dagger compiler
  ksp(libs.hilt.compiler)

  implementation(libs.coil)

  implementation(libs.androidx.fragment.ktx)

  implementation(libs.androidx.paging.runtime.ktx)

  implementation(libs.androidx.dataStore.core)
  implementation(libs.protobuf.kotlin.lite)
  implementation(libs.kotlin.reflect)

}
