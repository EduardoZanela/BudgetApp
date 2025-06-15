plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.0"
    alias(libs.plugins.kotlinJvm)
    application
}

group = "com.eduardozanela.budget"
version = "1.0.0"
application {
    mainClass.set("com.eduardozanela.budget.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(libs.picocli)
    implementation(libs.pdfbox)
    implementation(libs.tess4j)
    testImplementation(libs.kotlin.testJunit)
}