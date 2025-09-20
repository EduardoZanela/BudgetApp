import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
}

group = "com.eduardozanela.budget"
version = "1.0.0"

// Configure Java toolchain for JDK 21
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.picocli)
    implementation(libs.pdfbox)
    implementation(libs.tess4j)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.spring.boot.starter.test)
}

// Configure Kotlin compilation options
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions{
        jvmTarget = JvmTarget.JVM_21 // Ensure Kotlin compiles for JVM 21
    }
}

plugins.withType<org.springframework.boot.gradle.plugin.SpringBootPlugin> {
    tasks.named<BootJar>("bootJar") {
        // ===================================================================
        // Manage Build Variants (e.g., 'dev' or 'prod') at Build Time
        // ===================================================================
        // Define a custom Gradle property to control the build variant.
        // You can pass this property via the command line:
        // ./gradlew bootJar -PbuildVariant=prod
        // ./gradlew bootJar -PbuildVariant=dev (or default if not specified)
        // val buildVariant = project.findProperty("buildVariant")?.toString() ?: "dev"

        // Customize the output JAR filename based on the build variant
        //archiveFileName.set("server-${version.toString()}.jar")
        archiveFileName.set("budget-server.jar")
    }
}