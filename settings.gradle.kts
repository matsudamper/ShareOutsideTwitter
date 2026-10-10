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
            version("versionCode", "4")
            version("versionName", "1.0.0")

            version("android.sdk.min", "24")
            version("android.sdk.target", "33")
            version("android.sdk.compile", "37")

            library("core_ktx", "androidx.core:core-ktx:1.19.1")

            val junitVersion = "4.13.2"
            library("junit", "junit:junit:$junitVersion")

            library("androidx.test.ext.junit", "androidx.test.ext:junit:1.3.0")
            library("espresso.core", "androidx.test.espresso:espresso-core:3.7.0")
            library("lifecycle.runtime.ktx", "androidx.lifecycle:lifecycle-runtime-ktx:2.11.0")
            library("activity.compose", "androidx.activity:activity-compose:1.13.0")
            library("android.navigation.compose", "androidx.navigation:navigation-compose:2.10.2")
            library("gms.ads", "com.google.android.gms:play-services-ads:25.5.0")

            library("firebase.bom", "com.google.firebase:firebase-bom:35.0.0")
            library("firebase.crashlytics", "com.google.firebase", "firebase-crashlytics").withoutVersion()
            library("firebase.analytics", "com.google.firebase", "firebase-analytics").withoutVersion()
            library("firebase.crashlytics.plguin", "com.google.firebase", "firebase-crashlytics-gradle").version("3.0.8")

            // BOM
            library("compose.bom", "androidx.compose:compose-bom:2026.09.00")
            library("ui", "androidx.compose.ui", "ui").withoutVersion()
            library("ui.graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("ui.tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("ui.tooling.preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("ui.test.manifest", "androidx.compose.ui", "ui-test-manifest").withoutVersion()
            library("ui.test.junit4", "androidx.compose.ui", "ui-test-junit4").withoutVersion()
            library("material3", "androidx.compose.material3", "material3").withoutVersion()

            library("coil.compose", "io.coil-kt:coil-compose:2.7.0")

            plugin("androidApplication", "com.android.application").version("9.4.1")
            plugin("androidLibrary", "com.android.library").version("9.4.1")
            val kotlinVersion = "2.4.21"
            plugin("kotlinAndroid", "org.jetbrains.kotlin.android").version(kotlinVersion)
            plugin("composeCompiler", "org.jetbrains.kotlin.plugin.compose").version(kotlinVersion)
            plugin("appdistribution", "com.google.firebase.appdistribution").version("5.3.0")
            plugin("googleServices", "com.google.gms.google-services").version("4.5.0")
            plugin("playPublisher", "com.github.triplet.play").version("4.1.1")
        }
    }
}

rootProject.name = "Share outside Twitter"
include(":app")
include(":compose")
include(":resources")
include(":base")
