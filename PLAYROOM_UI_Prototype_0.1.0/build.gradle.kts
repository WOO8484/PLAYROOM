// PLAYROOM UI Prototype - root build file.
// Plugins are declared here with apply false so version-catalog-managed
// plugin versions are resolved once and reused by every module through
// the convention plugins in build-logic.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
