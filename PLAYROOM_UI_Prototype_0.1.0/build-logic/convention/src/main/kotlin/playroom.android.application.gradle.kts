import com.android.build.api.dsl.ApplicationExtension
import com.playroom.buildlogic.configureKotlinAndroid
import com.playroom.buildlogic.configureTestDependencies

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

extensions.configure<ApplicationExtension> {
    configureKotlinAndroid(this)
    defaultConfig {
        targetSdk = 35
    }
}

configureTestDependencies()
