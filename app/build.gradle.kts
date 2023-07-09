import com.android.build.api.dsl.ApplicationBuildType
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.appdistribution)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.playPublisher)
}

android {
    namespace = "net.matsudamper.shareoutside.bluebird"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    signingConfigs {
        create("release") {
            val storeFilePath = System.getenv("STORE_FILE")
            if (storeFilePath != null) {
                storeFile = file(storeFilePath)
                storePassword = System.getenv("STORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.target.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            firebaseAppDistribution {
                artifactType = "APK"
                serviceCredentialsFile = System.getenv("FIREBASE_SERVICE_CREDENTIALS_FILE")
            }
            buildValues(
                adMobApplicationID = System.getenv("ADMOB_APPLICATION_ID").orEmpty(),
                adMobIdShare = System.getenv("ADMOB_ID_SHARE").orEmpty(),
            )
        }
        debug {
            buildValues(
                adMobApplicationID = "ca-app-pub-3940256099942544~3347511713",
                adMobIdShare = "ca-app-pub-3940256099942544/6300978111",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun ApplicationBuildType.buildValues(
    adMobApplicationID: String,
    adMobIdShare: String,
) {
    addManifestPlaceholders(
        mapOf(
            "ADMOB_APPLICATION_ID" to adMobApplicationID,
        ),
    )
    buildConfigField("String", "ADMOB_ID_SHARE", "\n" + adMobIdShare + "\n")
}

play {
    val filePath = System.getenv("PLAY_STORE_PUBLISH_CREDENTIAL_FILE")
    if (filePath != null) {
        serviceAccountCredentials.set(
            file(filePath),
        )
    }

    track.set("beta")
    defaultToAppBundles.set(true)
    userFraction.set(1.0)
}

dependencies {
    implementation(project(":compose"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
}

tasks.create("buildAndRelease") {
    dependsOn("bundleRelease", "publishReleaseBundle")
}

tasks.create("buildAndAppDistribution") {
    dependsOn("assembleRelease", "appDistributionUploadRelease")
}
