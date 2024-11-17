plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

}
//apply(plugin = "kotlin-kapt")
//apply(plugin = "realm-android")

apply(plugin="kotlin-android")


android {
    namespace = "com.example.expensetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expensetracker"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true

    }
    kotlinOptions {
        jvmTarget = "17"
    }


}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.places)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation( libs.parkdatetimepicker)
    implementation(libs.flexbox)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    //add below dependancy for using room.
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.core:core:1.2.0")
    implementation("joda-time:joda-time:2.10.10")
    implementation("org.greenrobot:eventbus:3.1.1")
    implementation("org.apache.commons:commons-lang3:3.6")
    implementation("com.airbnb.android:lottie:3.4.0")

    //  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:")



}
