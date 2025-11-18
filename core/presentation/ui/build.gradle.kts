plugins {
    alias(libs.plugins.gumrun.android.library.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gumrun.jvm.ktor)
}

android {
    namespace = "com.example.presentation.ui"
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    api(libs.androidx.compose.material3)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}