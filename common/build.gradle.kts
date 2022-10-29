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

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
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
                api(compose.materialIconsExtended)
                api("io.github.jan-tennert.supabase:gotrue-kt:${Versions.SUPABASE}")
                api("io.github.jan-tennert.supabase:storage-kt:${Versions.SUPABASE}")
                api("io.github.jan-tennert.supabase:realtime-kt:${Versions.SUPABASE}")
                api("io.github.jan-tennert.supabase:postgrest-kt:${Versions.SUPABASE}")
                api("com.squareup.sqldelight:coroutines-extensions:${Versions.SQLDELIGHT}")
                api("io.insert-koin:koin-core:${Versions.KOIN}")
                api("com.russhwolf:multiplatform-settings-no-arg:${Versions.SETTINGS}")
                api("com.russhwolf:multiplatform-settings-coroutines:${Versions.SETTINGS}")
                api("com.arkivanov.essenty:instance-keeper:${Versions.ESSENTY}")
                api("io.ktor:ktor-client-cio:${Versions.KTOR}")
                api("io.github.g0dkar:qrcode-kotlin:${Versions.QR_CODE}")
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
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}")
                api("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.LIFECYCLE}")
            }
        }
        val androidTest by getting
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
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    }
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(31)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sqldelight {
    database("EinkaufszettelDatabase") {
        packageName = "io.github.jan.einkaufszettel.common.data.local"
    }
}