plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android.ksp)
}

android {
    namespace = "dev.nordix.discovery"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kotlin {
    //...
    sourceSets.all {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }
    //...
}

dependencies {
    implementation(project(":core"))
    implementation(project(":component:services"))
    implementation(project(":component:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.hilt.android)
    implementation(project(":component:settings"))
    implementation(project(":component:network:service_manager"))
    ksp(libs.hilt.compiler)
}