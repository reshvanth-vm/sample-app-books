import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.*
import org.gradle.api.project.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

class AndroidRoomConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("com.google.devtools.ksp")

//      extensions.configure<KspExtension> {
//        arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
//      }

      val roomLibModules = setOf("runtime", "ktx", "paging")
      val roomLib = "androidx.room"
      dependencies {
        ksp(lib("$roomLib.compiler"))
        roomLibModules.forEach {
          implementation(lib("$roomLib.$it"))
        }
      }
    }
  }

  /**
   * https://issuetracker.google.com/issues/132245929
   * [Export schemas](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schemas)
   */
  class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
  ) : CommandLineArgumentProvider {
    override fun asArguments() = listOf("room.schemaLocation=${schemaDir.path}")
  }
}