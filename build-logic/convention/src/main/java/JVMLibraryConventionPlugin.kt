import androidx.room.gradle.RoomExtension
import com.example.convention.configureKotlinJVM
import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class JVMLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.jvm")
                apply("com.google.devtools.ksp")
            }

            configureKotlinJVM()
        }
    }
}