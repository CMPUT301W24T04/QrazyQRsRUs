plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.qrazyqrsrus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qrazyqrsrus"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Navigation
    //from https://developer.android.com/guide/navigation#kts on Feb. 19, 2024
    val nav_version = "2.7.7"

    // Java language implementation
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //ZXing QR Codes
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    //firestore
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-firestore")

    //push notifications
    //https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner on February 28th, 2024
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    //photo picker
    implementation("androidx.activity:activity:1.8.2")
}