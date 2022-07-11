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
    // hilt
    implementation("com.google.dagger:hilt-android:2.42")
    implementation(project(mapOf("path" to ":base")))
    implementation(project(mapOf("path" to ":interactors")))
    implementation(project(mapOf("path" to ":localData")))
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(mapOf("path" to ":preferecnes")))
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    // navigation
    val navVersion = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // paging
    val pagingVersion = "3.1.1"
    implementation("androidx.paging:paging-runtime:$pagingVersion")

    val lifecycleVersion = "2.4.1"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // rv selection
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    // coil
    implementation("io.coil-kt:coil:2.1.0")

    // lifecycle service
    implementation("androidx.lifecycle:lifecycle-service:2.5.0")

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
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
