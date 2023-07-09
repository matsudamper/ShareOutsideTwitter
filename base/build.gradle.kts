import org.gradle.api.tasks.testing.logging.TestLogEvent
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.LibraryBuildType

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "net.matsudamper.shareoutside.bluebird.base"

    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        testHandleProfiling = true
        testFunctionalTest = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.testLogging.events = setOf(TestLogEvent.FAILED)
        }
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildValues(
                adMobIdShare = System.getenv("ADMOB_ID_SHARE").orEmpty(),
            )
        }
        debug {
            buildValues(
                adMobIdShare = "ca-app-pub-3940256099942544/6300978111",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

fun LibraryBuildType.buildValues(
    adMobIdShare: String,
) {
    buildConfigField("String", "ADMOB_ID_SHARE", "\"" + adMobIdShare + "\"")
}


dependencies {
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
}
