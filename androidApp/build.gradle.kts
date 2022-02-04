plugins {
    id("com.android.application")
    kotlin("android")
}



android {
    compileSdk = 32
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.linussudoku.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.4.0")

    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    //implementation("androidx.lifecycle:extensions:1.1.1")

}

