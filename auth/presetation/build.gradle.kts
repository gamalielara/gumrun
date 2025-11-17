plugins {
    alias(libs.plugins.gumrun.android.feature.ui)
}

android {
    namespace = "com.example.auth.presetation"

}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}