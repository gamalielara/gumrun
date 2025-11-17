plugins {
    alias(libs.plugins.gumrun.android.library)
}

android {
    namespace = "com.example.run.network"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
}