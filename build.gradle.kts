// Top-level build file: configuration shared by all modules.
// Plugins are declared here with "apply false" and applied in app/build.gradle.kts.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // AGP 9 has Kotlin built in, so we do NOT apply "org.jetbrains.kotlin.android".
        // The Kotlin Gradle plugin version is pinned here (the documented way) so it
        // matches the Compose compiler plugin version in gradle/libs.versions.toml.
        // KEEP THIS EQUAL to `kotlin = "..."` in gradle/libs.versions.toml.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
