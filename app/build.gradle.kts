plugins {
    alias(libs.plugins.android.application)
    // AGP 9 compiles Kotlin by itself ("built-in Kotlin"), so there is no kotlin-android plugin.
    // This plugin turns on the Compose compiler. Its version must equal the Kotlin version.
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.timezen"

    // compileSdk = the Android version we compile against (can be newer than targetSdk).
    // Newest AndroidX libraries usually require the newest platform to compile.
    compileSdk = 37

    defaultConfig {
        applicationId = "com.timezen.app"

        // minSdk 26 (Android 8.0): needed for notification channels and the
        // TYPE_APPLICATION_OVERLAY window type used by the floating timer (Phase 7).
        minSdk = 26

        // Google Play requires targetSdk 36 for new apps and updates from 2026-08-31.
        targetSdk = 36

        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            // Release signing is configured in Phase 12.
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM: picks matching versions for all androidx.compose libraries.
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
}
