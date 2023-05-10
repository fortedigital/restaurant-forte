/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 */

plugins {
    val kotlinVersion = "1.8.21"
    val ktorVersion = "2.3.0"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("io.ktor.plugin") version ktorVersion


    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

allprojects {
    repositories {
        // Use Maven Central for resolving dependencies.
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.gradle.application")

    dependencies {
        val kotlinVersion: String by project

        // Use the Kotlin JUnit 5 integration.
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")


        // Use the JUnit 5 integration.
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")

        // Serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

        // Datetime
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")


        // Logging
        implementation("ch.qos.logback:logback-classic:1.4.7")
    }

    kotlin {
        jvmToolchain(17)
    }

    tasks {
        test {
            useJUnitPlatform()
            testLogging {
                events("skipped", "failed")
            }
        }
    }
}

