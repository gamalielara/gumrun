import com.android.build.gradle.LibraryExtension
import com.example.convention.ExtensionType
import com.example.convention.configureAndroidCompose
import com.example.convention.configureKotlinAndroid
import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("gumrun.android.library")
            }

            val extensions = extensions.getByType<LibraryExtension>()

            configureAndroidCompose(extensions)
        }
    }
}
