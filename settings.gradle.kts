pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "StarlightExpedition"

include(":app")
include(":core:model")
include(":core:data")
include(":core:designsystem")
include(":core:navigation")
include(":core:common")
include(":feature:quickstart")
include(":feature:home")
include(":feature:favorites")
include(":feature:gamelist")
include(":feature:settings")
