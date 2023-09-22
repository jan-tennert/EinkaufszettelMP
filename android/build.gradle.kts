@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "io.github.jan.einkaufszettel"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}")
}

kotlin {
    jvmToolchain(11)
}

android {
    compileSdk = 34
    namespace = "io.github.jan.shopping"
    defaultConfig {
        applicationId = "io.github.jan.shopping"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
        debug {
            //  isMinifyEnabled = true
        }
    }
    lint {
        abortOnError = false
    }
}

compose {
   // kotlinCompilerPlugin.set("org.jetbrains.compose.compiler:compiler:${Versions.COMPOSE_COMPILER}") // see versions here https://mvnrepository.com/artifact/org.jetbrains.compose.compiler/compiler
}