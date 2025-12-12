plugins {
    alias(libs.plugins.gumrun.android.library)
    alias(libs.plugins.gumrun.jvm.ktor)
}

android {
    namespace = "com.example.core.data"
}

dependencies {
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.bundles.koin)

}