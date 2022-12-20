plugins {
    kotlin("jvm")
}
dependencies {
    implementation(kotlin("stdlib-common"))

    implementation(project(":ok-subscription-api-logs"))
    implementation(project(":ok-subscription-common"))
    implementation(project(":ok-subscription-mappers-v1"))
}

