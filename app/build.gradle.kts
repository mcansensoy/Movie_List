plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.omdb"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.omdb"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OMDB_API_KEY", "\"b215de35\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    //implementation(platform("androidx.compose:compose-bom:2025.08.01"))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /*implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")*/
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")
    // Hilt + Compose helper
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0") // önceden 1.2.0 idi sıkıntı çıkarsa geri al

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    // Lifecycle (ViewModel scope vs.) önceden 2.8.7 idi sıkıntı çıkarsa geri al
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.3") // :contentReference[oaicite:4]{index=4}
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.3")   // :contentReference[oaicite:5]{index=5}

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")    // :contentReference[oaicite:6]{index=6}
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2") // :contentReference[oaicite:7]{index=7}

    // Retrofit + Moshi converter (öneri: Moshi; istersen Gson'a tek satırla geçersin)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")                   // :contentReference[oaicite:8]{index=8}
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    /*implementation("com.squareup.retrofit2:converter-moshi:3.0.0")            // :contentReference[oaicite:9]{index=9}

    // Moshi (KotlinJsonAdapterFactory için)
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")                  // :contentReference[oaicite:10]{index=10}*/

    // OkHttp (Interceptor + logging)
    implementation("com.squareup.okhttp3:okhttp:5.1.0")                       // :contentReference[oaicite:11]{index=11}
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")          // :contentReference[oaicite:12]{index=12}

    // ViewModel + Compose entegrasyonu (gerekli; viewModel() fonksiyonunu sağlar) önceden 2.8.7 idi sıkıntı çıkarsa geri al
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.3")

// Compose içinde Flow/StateFlow güvenli tüketimi için (öneri) önceden 2.8.7 idi sıkıntı çıkarsa geri al
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.3")

    // --- Coil (image loading in Compose) ---
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- others (optional) ---
    implementation("com.google.code.gson:gson:2.13.1")

    //navigation compose
    implementation("androidx.navigation:navigation-compose:2.9.4")
}