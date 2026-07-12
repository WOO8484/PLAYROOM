plugins {
    id("playroom.android.application")
    id("playroom.android.compose")
}

android {
    namespace = "com.playroom.app"

    defaultConfig {
        applicationId = "com.playroom.app"
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = null
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))

    implementation(project(":feature:onboarding"))
    implementation(project(":feature:home"))
    implementation(project(":feature:library"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:search"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:player"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
}
