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

        register("androidLibrary") {
            id = "gumrun.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "gumrun.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUI") {
            id = "gumrun.android.library.feature.ui"
            implementationClass = "AndroidFeatureUIConventionPlugin"
        }

        register("androidRoom") {
            id = "gumrun.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("jvmLibrary") {
            id = "gumrun.jvm.library"
            implementationClass = "JVMLibraryConventionPlugin"
        }

        register("jvmKtor") {
            id = "gumrun.jvm.ktor"
            implementationClass = "JVMKtorConventionPlugin"
        }
    }
}
