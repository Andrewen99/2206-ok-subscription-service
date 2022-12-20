val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val testContainersVersion: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
}

group = rootProject.group
version = rootProject.version

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
//    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-websockets:$ktorVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation("io.ktor:ktor-client-auth:$ktorVersion")

    implementation(project(":ok-subscription-api-v1-jackson"))
    implementation(project(":ok-subscription-common"))
    implementation(project(":ok-subscription-mappers-v1"))
    implementation(project(":ok-subscription-stubs"))
    implementation(project(":ok-subscription-biz"))
    implementation(project(":ok-subscription-repo-inmemory"))
    implementation(project(":ok-subscription-repo-postgresql"))
    implementation(project(":ok-subscription-logging"))
    implementation(project(":ok-subscription-logs-mapper"))
    implementation(project(":ok-subscription-api-logs"))
}