plugins {
    kotlin("jvm")
}

group = "ru.otuskotlin.subscription"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val arcadeDbVersion: String by project
    val tinkerpopVersion: String by project
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project
    val testContainersVersion: String by project

    implementation(project(":ok-subscription-common"))
    implementation("com.benasher44:uuid:$kmpUUIDVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.apache.tinkerpop:gremlin-driver:$tinkerpopVersion")
    implementation("com.arcadedb:arcadedb-engine:$arcadeDbVersion")
    implementation("com.arcadedb:arcadedb-network:$arcadeDbVersion")
    implementation("com.arcadedb:arcadedb-gremlin:$arcadeDbVersion")

    testImplementation(project(":ok-subscription-repo-tests"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}