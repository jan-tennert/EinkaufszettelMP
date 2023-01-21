import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "io.github.jan.einkaufszettel"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
            )
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Rpm, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Einkaufszettel"
            packageVersion = "1.0.6"
            modules("java.sql")

            val iconsRoot = project.file("src/jvmMain/resources")
            windows {
                iconFile.set(iconsRoot.resolve("orders.ico"))
                shortcut = true
                perUserInstall = true
            }
            linux {
                shortcut = true
                iconFile.set(iconsRoot.resolve("orders.png"))
            }
        }
    }
}

compose {
   // kotlinCompilerPlugin.set("org.jetbrains.compose.compiler:compiler:${Versions.COMPOSE_COMPILER}") // see versions here https://mvnrepository.com/artifact/org.jetbrains.compose.compiler/compiler
}