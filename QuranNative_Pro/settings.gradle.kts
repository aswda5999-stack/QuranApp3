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

rootProject.name = "QuranNative_Pro"

include(":app")
include(":core:data")
include(":core:domain")
include(":core:designsystem")

include(":feature:quran")
include(":feature:audio")
include(":feature:azkar")
include(":feature:hadith")
include(":feature:prayer")
include(":feature:search")
