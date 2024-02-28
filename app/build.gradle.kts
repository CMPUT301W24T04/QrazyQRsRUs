plugins {
    id("com.android.application")
    //FIRESTORE
    id("com.google.gms.google-services")
//    id("com.google.gms.google-services")
    //id("com.google.gms.google-services")
}

android {
    namespace = "com.example.crazyqrtest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.crazyqrtest"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // ADDED NAVIGATION DEPENDECIES
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    implementation("com.google.firebase:firebase-firestore:24.10.2")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // FIRE STORE
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
//    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
//    implementation("com.google.firebase:firebase-firestore")
}