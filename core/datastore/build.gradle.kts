plugins {
  alias(libs.plugins.books.core.android)
  alias(libs.plugins.protobuf)
}

android {
  namespace = "com.examples.books.core.datastore"
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
  implementation(libs.androidx.dataStore.core)
  implementation(libs.protobuf.kotlin.lite)
}
