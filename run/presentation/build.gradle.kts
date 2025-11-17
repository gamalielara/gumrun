plugins {
    alias(libs.plugins.gumrun.android.feature.ui)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.run.presentation"
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}