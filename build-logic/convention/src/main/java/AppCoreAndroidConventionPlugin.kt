import org.gradle.api.*
import org.gradle.api.project.*
import org.gradle.kotlin.dsl.dependencies

class AppCoreAndroidConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("app.android.library")
        apply("app.android.hilt")
      }

      dependencies {
        implementation(project(":common:core"))

        implementation(lib("androidx.paging.runtime.ktx"))
      }
    }
  }
}