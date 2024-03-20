plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.bitkor.app"

    defaultConfig {
        applicationId = "com.bitkor1.app"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    // Tests can be Robolectric or instrumented tests
    sourceSets {
        val sharedTestDir = "src/sharedTest/java"
        getByName("test") {
            java.srcDir(sharedTestDir)
        }
        getByName("androidTest") {
            java.srcDir(sharedTestDir)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    dependencies {
        val composeBom = platform(libs.androidx.compose.bom)
        implementation(composeBom)
        androidTestImplementation(composeBom)

        implementation(libs.kotlin.stdlib)
        implementation(libs.kotlinx.coroutines.android)

        implementation(libs.androidx.compose.animation)
        implementation(libs.androidx.compose.foundation.layout)
        implementation(libs.androidx.compose.material.iconsExtended)
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.materialWindow)
        implementation(libs.androidx.compose.runtime.livedata)
        implementation(libs.androidx.compose.ui.tooling.preview)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
        debugImplementation(libs.androidx.compose.ui.tooling)

        implementation(libs.okhttp3)
        implementation(libs.gson)
        implementation(libs.gson.serializers)

        implementation(libs.accompanist.swiperefresh)
        implementation(libs.accompanist.systemuicontroller)
        implementation(libs.accompanist.permissions)

        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.activity.compose)

        implementation(libs.androidx.glance)
        implementation(libs.androidx.glance.appwidget)
        implementation(libs.androidx.glance.material3)

        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.savedstate)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewModelCompose)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.window)

        implementation(libs.google.android.material)
        implementation("androidx.compose.ui:ui:1.6.3")
        // Tooling support (Previews, etc.)

        // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
        implementation("androidx.compose.foundation:foundation:1.6.3")
        // Material Design
        implementation("androidx.compose.material:material:1.6.3")
        // Material design icons
        implementation("androidx.compose.material:material-icons-core:1.6.3")
        implementation("androidx.compose.material:material-icons-extended:1.6.3")
        // Integration with observables
        implementation("androidx.compose.runtime:runtime-livedata:1.6.3")
        implementation("androidx.compose.runtime:runtime-rxjava2:1.6.3")

    }



}