pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://jitpack.io/")
        }
        maven("https://maven.hq.hydraulic.software")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("com.google.devtools.ksp").version(extra["ksp.version"] as String)
        id("com.squareup.sqldelight").version(extra["sqlDelight.version"] as String)
        id("org.jetbrains.kotlin.plugin.serialization").version(extra["kotlin.version"] as String)
    }
}

rootProject.name = "Einkaufszettel"

include(":android", ":desktop", ":common")
