
import java.util.Properties//corresponde al API tomorrow
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.parcelize)

    kotlin("kapt")
    alias(libs.plugins.google.dagger.hilt)
}
android {
    namespace = "com.innonorbelis.tomatoia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.innonorbelis.tomatoia"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {viewBinding = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    buildFeatures {
        buildConfig = true
        viewBinding = true

    }
}

dependencies {

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    kapt(libs.androidx.lifecycle.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // Hilt
    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // CardView
    implementation(libs.androidx.cardview)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Databinding
    implementation(libs.androidx.databinding.runtime)

// Camara X
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
// TensorFlowLite
    implementation (libs.tensorflow.lite)
    implementation (libs.tensorflow.lite.support)
    implementation (libs.tensorflow.lite.task.vision)

    // lottie animation procesamiento
    implementation(libs.lottie)
// Animación desplegable

        implementation(libs.transition)


}
