plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "better.text.protext"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // hilt
    implementation("com.google.dagger:hilt-android:2.42")
    implementation(project(mapOf("path" to ":base")))
    implementation(project(mapOf("path" to ":ui-bookmarks")))
    implementation(project(mapOf("path" to ":interactors")))
    implementation(project(mapOf("path" to ":repository")))
    implementation(project(mapOf("path" to ":localData")))
    implementation(project(mapOf("path" to ":remoteData")))
    implementation(project(mapOf("path" to ":ui-copiedTexts")))
    implementation(project(mapOf("path" to ":ui-settings")))
    implementation(project(mapOf("path" to ":preferecnes")))
//    implementation(project(mapOf("path" to ":base")))
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    // Navigation
    val navVersion = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}
