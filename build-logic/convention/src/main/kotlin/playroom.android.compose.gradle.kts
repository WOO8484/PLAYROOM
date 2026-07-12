import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.playroom.buildlogic.libs
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

// Works whether this module applied playroom.android.library or
// playroom.android.application first — looked up by instance rather than
// by a compile-time type-safe accessor so plugin application order of the
// *other* convention plugin never matters.
val commonExtension: CommonExtension<*, *, *, *, *, *> =
    extensions.findByType(LibraryExtension::class.java)
        ?: extensions.findByType(ApplicationExtension::class.java)
        ?: error(
            "playroom.android.compose must be applied together with " +
                "playroom.android.library or playroom.android.application",
        )

commonExtension.apply {
    buildFeatures.compose = true
}

dependencies {
    val bom = libs.findLibrary("androidx-compose-bom").get()
    add("implementation", platform(bom))
    add("androidTestImplementation", platform(bom))
    add("implementation", libs.findLibrary("androidx-compose-ui").get())
    add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
    add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    add("implementation", libs.findLibrary("androidx-compose-material3").get())
    add("implementation", libs.findLibrary("androidx-compose-foundation").get())
    add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
    add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
}
