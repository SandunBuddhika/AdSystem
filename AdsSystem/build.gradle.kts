plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.sandun.adssystem"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation("com.google.errorprone:error_prone_annotations:2.36.0")

    implementation ("com.google.android.gms:play-services-ads:23.5.0")
    implementation ("androidx.annotation:annotation:1.0.0")
    implementation ("com.facebook.android:audience-network-sdk:6.18.0")
}