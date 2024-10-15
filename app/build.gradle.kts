plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pegalite.fivegonly"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pegalite.fivegonly"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    /* For Responsive Layout*/
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    /*For Ads*/
    implementation(libs.play.services.ads)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}