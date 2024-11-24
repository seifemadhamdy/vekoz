import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "seifemadhamdy.vekoz"
    compileSdk = 35

    defaultConfig {
        applicationId = "seifemadhamdy.vekoz"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        rootProject.file("tmdb.properties").inputStream().use { fileInputStream ->
            val tmdbProperties = Properties()

            tmdbProperties.apply {
                load(fileInputStream)

                buildConfigField(
                    type = "String",
                    name = "TMDB_ACCESS_TOKEN",
                    value = getProperty("TMDB_ACCESS_TOKEN"),
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }

    kotlinOptions { jvmTarget = "22" }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.text.google.fonts)

    // Networking and API
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Room compiler
    ksp(libs.androidx.room.compiler)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)

    // Miscellaneous
    implementation(libs.haze)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}
