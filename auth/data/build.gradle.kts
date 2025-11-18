plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.gumrun.jvm.ktor)
}

android {
    namespace = "com.example.auth.data"
    
}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}