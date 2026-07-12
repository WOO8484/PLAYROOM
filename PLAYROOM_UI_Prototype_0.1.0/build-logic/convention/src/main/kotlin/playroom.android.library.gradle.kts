import com.android.build.api.dsl.LibraryExtension
import com.playroom.buildlogic.configureKotlinAndroid
import com.playroom.buildlogic.configureTestDependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

extensions.configure<LibraryExtension> {
    configureKotlinAndroid(this)
}

configureTestDependencies()
