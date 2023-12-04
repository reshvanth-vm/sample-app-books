import org.gradle.api.*
import org.gradle.api.project.*
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.google.devtools.ksp")
        apply("dagger.hilt.android.plugin")
      }

      dependencies {
        implementation(lib("hilt.android"))
        "ksp"(lib("hilt.compiler"))
      }
    }
  }
}
