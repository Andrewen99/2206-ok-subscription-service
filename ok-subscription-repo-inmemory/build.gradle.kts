plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
//    macosX64 {}
    linuxX64 {}

    sourceSets {
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project
        val cache4kVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":ok-subscription-common"))
                implementation("com.benasher44:uuid:$kmpUUIDVersion")
                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation(project(":ok-subscription-repo-tests"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                api(kotlin("test-junit"))
            }
        }
    }
}