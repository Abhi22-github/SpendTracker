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
    compileSdk = 34

    defaultConfig {
        applicationId = "com.roaa.expensetracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "0.2"
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
    implementation(libs.ui.tooling.preview)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.parkdatetimepicker)
    implementation(libs.flexbox)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    //add below dependancy for using room.
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.core)
    implementation(libs.joda.time)
    implementation(libs.commons.lang3)
    implementation(libs.lottie)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    //  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore:1.1.2")

    implementation("com.airbnb.android:lottie-compose:6.0.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.25.1")

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ui)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")


//for Month view compose
    implementation("androidx.compose.foundation:foundation:1.7.6")
    implementation("com.google.accompanist:accompanist-pager:0.32.0") // For HorizontalPager
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")

//firebase
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation("androidx.core:core-splashscreen:1.0.1")

}
