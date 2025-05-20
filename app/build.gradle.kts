plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.mini.assessment_profile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mini.assessment_profile"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // ViewModel & LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.v190)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room dependencies with exclusion for duplicate annotations
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.room.common.jvm)
    ksp(libs.androidx.room.compiler)
    // Use annotationProcessor instead of direct implementation for the compiler
    annotationProcessor(libs.androidx.room.compiler) {
        exclude(group = "com.intellij", module = "annotations")
    }

    // Add newer annotations explicitly
    implementation(libs.annotations)

    // Configuration to exclude the older annotations from all dependencies
    configurations.all {
        resolutionStrategy {
            // Force use of the newer annotations library
            force("org.jetbrains:annotations:23.0.0")
        }
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}