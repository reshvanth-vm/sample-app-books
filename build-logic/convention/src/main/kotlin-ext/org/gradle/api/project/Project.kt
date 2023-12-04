package org.gradle.api.project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private object C {
  val javaVersion = JavaVersion.VERSION_11
}

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *>) {
  commonExtension.apply {
    compileSdk = 34

    defaultConfig {
      minSdk = 24
    }

    compileOptions {
      sourceCompatibility = C.javaVersion
      targetCompatibility = C.javaVersion
//      isCoreLibraryDesugaringEnabled = true
    }
  }

  configureKotlin()
}

internal fun Project.configureKotlinJvm() {
  extensions.configure<JavaPluginExtension> {
    sourceCompatibility = C.javaVersion
    targetCompatibility = C.javaVersion
  }

  configureKotlin()
}

private fun Project.configureKotlin() {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = C.javaVersion.toString()

//            val warningsAsErrors: String? by project
//            allWarningsAsErrors = warningsAsErrors.toBoolean()

      freeCompilerArgs = freeCompilerArgs + listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
      )
    }
  }

  dependencies {
    implementation(lib("kotlinx.coroutines.core"))
  }

}

val Project.libs
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.lib(id: String): Provider<MinimalExternalModuleDependency> {
  return libs.findLibrary(id).get()
}

internal fun DependencyHandlerScope.implementation(dep: Provider<MinimalExternalModuleDependency>) {
  add("implementation", dep)
}

internal fun DependencyHandlerScope.implementation(project: Project) {
  add("implementation", project)
}

internal fun DependencyHandlerScope.ksp(dep: Provider<MinimalExternalModuleDependency>) {
  add("ksp", dep)
}