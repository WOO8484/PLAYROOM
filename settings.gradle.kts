pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "PlayroomUiPrototype"

include(":app")

include(":core:model")
include(":core:designsystem")
include(":core:data")
include(":core:navigation")

include(":feature:onboarding")
include(":feature:home")
include(":feature:library")
include(":feature:favorites")
include(":feature:search")
include(":feature:settings")
include(":feature:player")
