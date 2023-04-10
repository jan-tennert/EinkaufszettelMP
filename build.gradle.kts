group = "io.github.jan.einkaufszettel"
version = "1.0-SNAPSHOT"

buildscript {
    extra["kotlin_version"] = "1.8.20"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://jitpack.io/")
        }
        mavenLocal()
        maven {
            url = uri("https://androidx.dev/storage/compose-compiler/repository/")
        }
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("com.google.devtools.ksp") apply false
    id("com.squareup.sqldelight") apply false
    kotlin("plugin.serialization") apply false
}