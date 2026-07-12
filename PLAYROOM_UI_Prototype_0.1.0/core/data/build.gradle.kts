plugins {
    id("playroom.android.library")
}

android {
    namespace = "com.playroom.core.data"
}

dependencies {
    api(project(":core:model"))
    implementation(libs.kotlinx.coroutines.core)
}
