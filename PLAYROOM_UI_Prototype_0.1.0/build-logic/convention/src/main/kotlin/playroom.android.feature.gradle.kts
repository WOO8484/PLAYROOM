import com.playroom.buildlogic.libs
import org.gradle.kotlin.dsl.dependencies

// Convenience convention for every `:feature:*` module: an Android library
// with Compose enabled, plus the core modules and Compose-navigation /
// lifecycle libraries every feature needs. Individual feature modules only
// add feature-specific dependencies (if any) on top of this.
plugins {
    id("playroom.android.library")
    id("playroom.android.compose")
}

dependencies {
    add("implementation", project(":core:model"))
    add("implementation", project(":core:designsystem"))
    add("implementation", project(":core:data"))
    add("implementation", project(":core:navigation"))

    add("implementation", libs.findLibrary("androidx-core-ktx").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
    add("implementation", libs.findLibrary("androidx-navigation-compose").get())
    add("implementation", libs.findLibrary("androidx-activity-compose").get())
}
