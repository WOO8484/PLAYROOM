package com.playroom.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/** Shortcut to reach the `libs` version catalog from any convention script. */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Shared Android + Kotlin configuration applied to every module in the
 * project (library or application). Keeps compileSdk / minSdk / Java
 * version / Kotlin jvmTarget consistent in a single place so individual
 * module build.gradle.kts files never repeat this boilerplate.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 26
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    extensions.getByType<KotlinAndroidProjectExtension>().apply {
        jvmToolchain(17)
    }
}

/**
 * Common unit-test dependencies shared by every module (JUnit4 + kotlinx
 * coroutines test helpers). Applied from the library/application
 * convention plugins so `testImplementation` never needs to be repeated
 * in each module.
 */
internal fun Project.configureTestDependencies() {
    dependencies {
        add("testImplementation", libs.findLibrary("junit").get())
        add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
    }
}
