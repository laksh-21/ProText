plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
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
}

dependencies {
    implementation(project(mapOf("path" to ":localData")))
    implementation(project(mapOf("path" to ":remoteData")))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    testImplementation("junit:junit:4.13.2")

    // hilt
    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.41")

    // paging
    val pagingVersion = "3.1.1"
    implementation("androidx.paging:paging-runtime:$pagingVersion")

    kapt("com.google.dagger:hilt-android-compiler:2.41")
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}
