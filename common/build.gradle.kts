@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization")
}

group = "io.github.jan.einkaufszettel"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://jitpack.io") }
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
            )
        }
        jvmToolchain(11)
    }
    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
              //  api(compose.materialIconsExtended)
                api(platform("io.github.jan-tennert.supabase:bom:${Versions.SUPABASE}"))
                api("io.github.jan-tennert.supabase:gotrue-kt")
                api("io.github.jan-tennert.supabase:storage-kt")
                api("io.github.jan-tennert.supabase:realtime-kt")
                api("io.github.jan-tennert.supabase:postgrest-kt")
                api("io.github.jan-tennert.supabase:functions-kt")
                api("com.squareup.sqldelight:coroutines-extensions:${Versions.SQLDELIGHT}")
                api("io.insert-koin:koin-core:${Versions.KOIN}")
                api("com.russhwolf:multiplatform-settings-no-arg:${Versions.SETTINGS}")
                api("com.russhwolf:multiplatform-settings-coroutines:${Versions.SETTINGS}")
                api("com.arkivanov.essenty:instance-keeper:${Versions.ESSENTY}")
                api("io.ktor:ktor-client-okhttp:${Versions.KTOR}")
                api("io.github.g0dkar:qrcode-kotlin:${Versions.QR_CODE}")
                implementation("com.github.stevdza-san:OneTapCompose:1.0.3")
                implementation("com.mohamedrejeb.richeditor:richeditor-compose:${Versions.RICH_TEXT}")
                implementation("io.ktor:ktor-client-logging:${Versions.KTOR}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:${Versions.COMPAT}")
                api("androidx.core:core-ktx:${Versions.KTX}")
                api("com.squareup.sqldelight:android-driver:${Versions.SQLDELIGHT}")
                api("io.insert-koin:koin-android:${Versions.KOIN_ANDROID}")
                api("androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}")
                api("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.LIFECYCLE}")
                api("com.google.accompanist:accompanist-permissions:${Versions.PERMISSIONS}")
                api("androidx.camera:camera-core:${Versions.CAMERAX}")
                api("androidx.camera:camera-camera2:${Versions.CAMERAX}")
                api("androidx.camera:camera-lifecycle:${Versions.CAMERAX}")
                api("androidx.camera:camera-view:${Versions.CAMERAX}")
                api("com.google.mlkit:barcode-scanning:${Versions.BARCODE}")
                api("io.insert-koin:koin-androidx-compose:3.5.0")
                api("io.coil-kt:coil-compose:2.4.0")
            }
        }
        val androidUnitTest by getting
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api("com.squareup.sqldelight:sqlite-driver:${Versions.SQLDELIGHT}")
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdk = 34
    namespace = "io.github.jan.shopping.common"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    }
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    lint {
        abortOnError = false
    }
    buildTypes {
        release {
            isMinifyEnabled = true
        //    isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
        debug {
            //  isMinifyEnabled = true
        }
    }
}

sqldelight {
    database("EinkaufszettelDatabase") {
        packageName = "io.github.jan.einkaufszettel.common.data.local"
    }
}

compose {
   // kotlinCompilerPlugin.set("org.jetbrains.compose.compiler:compiler:${Versions.COMPOSE_COMPILER}") // see versions here https://mvnrepository.com/artifact/org.jetbrains.compose.compiler/compiler
}