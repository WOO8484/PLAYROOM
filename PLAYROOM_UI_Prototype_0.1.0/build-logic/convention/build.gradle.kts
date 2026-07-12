plugins {
    `kotlin-dsl`
}

group = "com.playroom.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

// Precompiled script plugins under src/main/kotlin are auto-registered by
// `kotlin-dsl`; the file name (without .gradle.kts) becomes the plugin id.
// No explicit gradlePlugin { plugins { ... } } block is required.
