plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.nordix.resourcesharing"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.nordix.resourcesharing"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }
}


dependencies {
    implementation(project(":core"))
    implementation(project(":common_ui"))

    implementation(project(":component:common"))
    implementation(project(":component:network:discovery"))
    implementation(project(":component:network:publish"))
    implementation(project(":component:network:server_provider"))
    implementation(project(":component:network:service_manager"))
    implementation(project(":component:services:core"))
    implementation(project(":component:services:presentation"))
    implementation(project(":component:settings"))

    implementation(project(":feature:network_map"))
    implementation(project(":feature:homescreen"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.ktor)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.extendedIcons)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.composeAnnotation)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.espresso.core)

    ksp(libs.hilt.compiler)
    ksp(libs.composeAnnotationProcessor)
    coreLibraryDesugaring(libs.coreLibraryDesugaring)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
