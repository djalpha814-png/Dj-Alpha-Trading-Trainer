plugins {
    id("com.android.application")
}

android {
    namespace = "com.djalphatradingtrainer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.djalphatradingtrainer"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
}
