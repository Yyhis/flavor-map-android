import java.util.Properties
import java.io.FileInputStream

// env
val properties =  Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "net.yyhis.flavormap.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.yyhis.flavormap.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // env
            // resValue("string", "kakao_native_app_key", properties["KAKAO_NATIVE_APP_KEY"] as String)
            buildConfigField("String", "kakao_native_app_key", properties["KAKAO_NATIVE_APP_KEY"] as String)
            buildConfigField("String", "naver_map_sdk_key", properties["NAVER_MAP_SDK_KEY"] as String)
            manifestPlaceholders["kakao_native_app_redirect"] = properties["KAKAO_NATIVE_APP_REDIRECT"] as String
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // env
            buildConfigField("String", "kakao_native_app_key", properties["KAKAO_NATIVE_APP_KEY"] as String)
            buildConfigField("String", "naver_map_sdk_key", properties["NAVER_MAP_SDK_KEY"] as String)
            manifestPlaceholders["kakao_native_app_redirect"] = properties["KAKAO_NATIVE_APP_REDIRECT"] as String
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("com.google.android.material:material:1.7.0")

    implementation ("androidx.compose.runtime:runtime-livedata:1.7.8")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    implementation ("com.jakewharton.timber:timber:5.0.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

    implementation ("com.kakao.sdk:v2-user:2.20.0")

//    implementation("com.naver.maps:map-sdk:3.20.0")
//
//    implementation("io.github.fornewid:naver-map-compose:1.7.2")
//    implementation("io.github.fornewid:naver-map-location:21.0.2")
}
