androidApplication {
    namespace = "org.example.app"

    dependencies {
        // AndroidX Core and UI
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.viewpager2:viewpager2:1.1.0")
        implementation("androidx.preference:preference-ktx:1.2.1")

        // Lifecycle components
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

        // Navigation
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

        // Room database
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        compileOnly("androidx.room:room-compiler:2.6.1")
        
        // Additional annotation support
        implementation("androidx.annotation:annotation:1.7.1")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
        implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
        implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
        implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
        implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.0")

        // Network
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
        implementation("com.google.code.gson:gson:2.10.1")

        // Work Manager
        implementation("androidx.work:work-runtime-ktx:2.9.1")

        // Color Picker
        implementation("com.github.yukuku:ambilwarna:2.0.1")

        // Security
        implementation("androidx.security:security-crypto:1.1.0-alpha06")
        implementation("androidx.biometric:biometric:1.2.0-alpha05")
        implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

        // Keep existing modules
        implementation(project(":utilities"))
        implementation(project(":list"))
    }
}
