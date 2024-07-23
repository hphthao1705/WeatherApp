plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 33
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
        viewBinding=true;
        dataBinding=true;
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("junit:junit:4.12")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    //viewmodel
    implementation("android.arch.lifecycle:extensions:1.1.1")
    annotationProcessor("android.arch.lifecycle:compiler:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.4.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")

    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    //fragment
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    //room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    //annotationProcessor "androidx.room:room-compiler:2.5.2"
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32");
    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:2.5.0")
    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:2.5.0")
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:2.5.0")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.5.0")
    //loading
    implementation("com.airbnb.android:lottie:3.4.0")
    //junit
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("com.android.support.test:rules:1.0.1")
    testImplementation("androidx.arch.core:core-testing:2.1.0") //instantTaskExecutorRule
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    //mockito
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    //coroutine-testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1")
    //local test
    testImplementation("org.robolectric:robolectric:4.9") //cho room test

    //fix: No instrumentation registered! Must run under a registering instrumentation.
    androidTestImplementation("androidx.test:runner:1.0.2")
    androidTestImplementation("androidx.test:rules:1.0.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.0.2")

    //test UI
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    //--espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    //test UI 2
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    //scroll recycler
    androidTestImplementation( "androidx.test.espresso:espresso-contrib:3.3.0") //add bản này để sử dụng // với matchers. Bản cao hơn bị xung đột với matchers.
    //Espresso idling resources
    implementation("androidx.test.espresso:espresso-idling-resource:3.1.0")
    //UIAutomator
    androidTestImplementation("com.android.support.test.uiautomator:uiautomator-v18:2.1.3")
    //Koin
    implementation("io.insert-koin:koin-android:3.2.0-beta-1")
    implementation("io.insert-koin:koin-androidx-compose:3.2.0-beta-1")
    //navigation bottom
    implementation("com.google.android.material:material:1.1.0")
    //Google Play Services
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //Fused Location Provider for Android
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //volley for json
    implementation("com.android.volley:volley:1.2.1")
}