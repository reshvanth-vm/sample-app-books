import org.gradle.api.*
import org.gradle.api.project.*
import org.gradle.kotlin.dsl.dependencies

class AppFeatureAndroidConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("app.android.library")
        apply("app.android.hilt")
      }

      dependencies {
        implementation(project(":core:common"))
        implementation(project(":core:datastore"))
        implementation(project(":core:domain"))
        implementation(project(":core:model"))
        implementation(project(":core:usecase"))

        implementation(lib("androidx.paging.runtime.ktx"))
      }
    }
  }
}
