plugins {
    alias(libs.plugins.gumrun.android.library)
    alias(libs.plugins.gumrun.android.room)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
    implementation(libs.mongodb.bson)
    implementation(projects.core.domain)
}