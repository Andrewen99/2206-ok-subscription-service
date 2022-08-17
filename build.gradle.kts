plugins {
    kotlin("jvm") apply false
}

group = "ru.otuskotlin.subscription"
version = "0.0.1-SNAPSHOT"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}
