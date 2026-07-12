plugins {
    id("playroom.android.library")
    id("playroom.android.compose")
}

android {
    namespace = "com.playroom.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.material.icons.extended)
}
