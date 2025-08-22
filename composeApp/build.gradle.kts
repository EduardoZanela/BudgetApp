@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.internal.extensions.core.extra
import java.io.File

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

// In the 'kotlin' block, you'll configure and use the new task
val generatedDir = layout.buildDirectory.dir("generated/kotlin/main")

abstract class GenerateBuildConfigTask : DefaultTask() {
    @get:Input
    abstract val apiUrl: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val fileContent = """
            package com.eduardozanela.budget.generated

            object Config {
                const val API_URL = "${apiUrl.get()}"
            }
        """.trimIndent()

        val outputDirectory = outputDir.asFile.get()
        outputDirectory.mkdirs()
        File(outputDirectory, "Config.kt").writeText(fileContent)
        println("Generated Config.kt with API_URL: ${apiUrl.get()}")
    }
}

tasks.register<GenerateBuildConfigTask>("generateBuildConfig") {
    val isProduction = project.gradle.startParameter.taskNames.any { it.contains("wasmJsBrowserDistribution", ignoreCase = true) }
    val apiUrlGenerated = if (isProduction) {
        "https://api.eduardozanela.com"
    } else {
        "http://localhost:8081"
    }
    apiUrl.set(apiUrlGenerated)
    outputDir.set(generatedDir)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path

            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonMain {
            kotlin.srcDir(generatedDir)
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.navigation.compose)

                // Use modern syntax for Compose dependencies
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(projects.shared)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        wasmJsMain {
            dependencies {
                // Ktor engine for wasmJs
                implementation(libs.ktor.client.js)
            }
        }
    }
}

// -------------------------------------------------------------------
// 4. TASK DEPENDENCIES
//    (This is the most critical part for a correct build order)
//// -------------------------------------------------------------------
tasks.named("preBuild") {
    dependsOn("generateBuildConfig")
}
tasks.named("compileKotlinWasmJs") {
    dependsOn("generateBuildConfig")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon> {
    dependsOn("generateBuildConfig")
}

tasks.named("wasmJsBrowserTest").configure {
    enabled = false
}

android {
    namespace = "com.eduardozanela.budget"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.eduardozanela.budget"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
