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
            packageVersion = "1.0.4"
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
