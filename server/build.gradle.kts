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
    //applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.reactivestreams:reactive-streams")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")
    implementation(libs.picocli)
    implementation(libs.pdfbox)
    implementation(libs.tess4j)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlin.testJunit)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}