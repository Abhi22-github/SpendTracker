plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.serialization)
    id("com.google.firebase.crashlytics")
    // Apply Hilt plugin
}
apply(plugin = "kotlin-kapt")
//apply(plugin = "realm-android")

apply(plugin = "kotlin-android")


android {
    namespace = "com.roaa.expensetracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.roaa.expensetracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 7
        versionName = "0.7"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    kotlinOptions {
        jvmTarget = "17"
    }


}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.androidx.material3)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.places)
    implementation(libs.core.ktx)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.android)

    //add below dependancy for using room.
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.core)
    implementation(libs.commons.lang3)
    implementation(libs.lottie)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.constraintlayout.compose)


//firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.androidx.core.splashscreen)

    //compose
    val composeBom = platform("androidx.compose:compose-bom:2025.01.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // or skip Material Design and build directly on top of foundational components
    implementation(libs.foundation)
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation(libs.androidx.material.icons.core)
    // Optional - Add full set of material icons
    implementation(libs.androidx.material.icons.extended)
    // Optional - Add window size utils
    implementation(libs.androidx.adaptive)

    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")

}
