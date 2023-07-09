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
            version("versionCode", "3")
            version("versionName", "1.0.0")

            version("android.sdk.min", "24")
            version("android.sdk.target", "33")
            version("android.sdk.compile", "33")
            version("android.buildTools", "33.0.1")
            version("android.compose.compiler", "1.4.3")

            library("core_ktx", "androidx.core:core-ktx:1.9.0")

            val junitVersion = "4.13.2"
            library("junit", "junit:junit:$junitVersion")

            library("androidx.test.ext.junit", "androidx.test.ext:junit:1.1.5")
            library("espresso.core", "androidx.test.espresso:espresso-core:3.5.1")
            library("lifecycle.runtime.ktx", "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
            library("activity.compose", "androidx.activity:activity-compose:1.7.2")
            library("android.navigation.compose", "androidx.navigation:navigation-compose:2.6.0")
            library("gms.ads", "com.google.android.gms:play-services-ads:20.6.0")

            library("firebase.bom", "com.google.firebase:firebase-bom:32.1.1")
            library("firebase.crashlytics", "com.google.firebase", "firebase-crashlytics-ktx").withoutVersion()
            library("firebase.analytics", "com.google.firebase", "firebase-analytics-ktx").withoutVersion()
            library("firebase.crashlytics.plguin", "com.google.firebase", "firebase-crashlytics-gradle").version("2.9.5")

            // BOM
            library("compose.bom", "androidx.compose:compose-bom:2023.03.00")
            library("ui", "androidx.compose.ui", "ui").withoutVersion()
            library("ui.graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("ui.tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("ui.tooling.preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("ui.test.manifest", "androidx.compose.ui", "ui-test-manifest").withoutVersion()
            library("ui.test.junit4", "androidx.compose.ui", "ui-test-junit4").withoutVersion()
            library("material3", "androidx.compose.material3", "material3").withoutVersion()

            library("coil.compose", "io.coil-kt:coil-compose:2.4.0")

            plugin("androidApplication", "com.android.application").version("8.0.2")
            plugin("kotlinAndroid", "org.jetbrains.kotlin.android").version("1.8.10")
            plugin("appdistribution", "com.google.firebase.appdistribution").version("4.0.0")
            plugin("googleServices", "com.google.gms.google-services").version("4.3.15")
            plugin("playPublisher", "com.github.triplet.play").version("3.8.4")
        }
    }
}

rootProject.name = "Share outside Twitter"
include(":app")
include(":compose")
include(":resources")
include(":base")
