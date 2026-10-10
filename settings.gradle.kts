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
    versionCatalogs {
        create("libs") {
            from(files("build-logic/libs.versions.toml"))
        }
    }
}

rootProject.name = "Share outside Twitter"
include(":app")
include(":compose")
include(":resources")
include(":base")
