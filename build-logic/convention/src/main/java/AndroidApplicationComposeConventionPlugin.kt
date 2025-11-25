import com.android.build.api.dsl.ApplicationExtension
import com.example.convention.configureAndroidCompose
import com.example.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        target.run {
            // Apply Application convention plugin with plugin manager
            pluginManager.run{
                apply("gumrun.androidApplication")
            }


            val extension = extensions.getByType<ApplicationExtension>()

            configureAndroidCompose(extension)
        }
    }
}