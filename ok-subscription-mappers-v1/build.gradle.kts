plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-subscription-api-v1-jackson"))
    implementation(project(":ok-subscription-common"))

    testImplementation(kotlin("test-junit"))
}