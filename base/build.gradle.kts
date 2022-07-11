plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32
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
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    // rv selection
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    // navigation
    val navVersion = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // paging
    val pagingVersion = "3.1.1"
    implementation("androidx.paging:paging-runtime:$pagingVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    implementation("androidx.lifecycle:lifecycle-common:2.5.0")

    // lifecycle service
    implementation("androidx.lifecycle:lifecycle-service:2.5.0")
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}
