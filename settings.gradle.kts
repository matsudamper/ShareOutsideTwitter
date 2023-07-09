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
            version("android.sdk.min", "24")
            version("android.sdk.target", "33")
            version("android.sdk.compile", "33")
            version("android.buildTools", "33.0.1")
            version("android.compose.compiler", "1.4.3")

            version("agp", "8.0.2")
            version("kotlin", "1.8.10")
            version("core_ktx", "1.9.0")
            version("junit", "4.13.2")
            version("androidx_test_ext_junit", "1.1.5")
            version("espresso_core", "3.5.1")
            version("lifecycle_runtime_ktx", "2.6.1")
            version("activity_compose", "1.7.2")
            version("compose_bom", "2023.03.00")

            library("core_ktx", "androidx.core", "core-ktx")
                .versionRef("core_ktx")
            library("junit", "junit", "junit").versionRef("junit")
            library("androidx_test_ext_junit", "androidx.test.ext", "junit")
                .versionRef("androidx_test_ext_junit")
            library("espresso_core", "androidx.test.espresso", "espresso-core")
                .versionRef("espresso_core")
            library("lifecycle_runtime_ktx", "androidx.lifecycle", "lifecycle-runtime-ktx")
                .versionRef("lifecycle_runtime_ktx")
            library("activity_compose", "androidx.activity", "activity-compose")
                .versionRef("activity_compose")
            library("android.navigation.compose", "androidx.navigation:navigation-compose:2.6.0")

            run {
                library("firebase.bom", "com.google.firebase:firebase-bom:32.1.1")
                library("firebase.crashlytics", "com.google.firebase", "firebase-crashlytics-ktx").withoutVersion()
                library("firebase.analytics", "com.google.firebase", "firebase-analytics-ktx").withoutVersion()
            }
            // BOM
            run {
                library("compose_bom", "androidx.compose", "compose-bom")
                    .versionRef("compose_bom")
                library("ui", "androidx.compose.ui", "ui")
                    .withoutVersion()
                library("ui_graphics", "androidx.compose.ui", "ui-graphics")
                    .withoutVersion()
                library("ui_tooling", "androidx.compose.ui", "ui-tooling")
                    .withoutVersion()
                library("ui_tooling_preview", "androidx.compose.ui", "ui-tooling-preview")
                    .withoutVersion()
                library("ui_test_manifest", "androidx.compose.ui", "ui-test-manifest")
                    .withoutVersion()
                library("ui_test_junit4", "androidx.compose.ui", "ui-test-junit4")
                    .withoutVersion()
                library("material3", "androidx.compose.material3", "material3")
                    .withoutVersion()
            }

            library("coil.compose", "io.coil-kt:coil-compose:2.4.0")

            plugin("androidApplication", "com.android.application").versionRef("agp")
            plugin("kotlinAndroid", "org.jetbrains.kotlin.android").versionRef("kotlin")
            plugin("appdistribution", "com.google.firebase.appdistribution").version("4.0.0")
            plugin("googleServices", "com.google.gms.google-services").version("4.3.15")
            plugin("playPublisher", "com.github.triplet.play").version("3.7.0")
        }
    }
}

rootProject.name = "Share outside Twitter"
include(":app")
include(":compose")
include(":resources")
