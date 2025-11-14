plugins {
    `kotlin-dsl`
}

group = "com.example.run.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gumrun.androidApplication"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "gumrun.androidApplication.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}