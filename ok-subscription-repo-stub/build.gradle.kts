plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
//    macosX64 {}
    linuxX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":ok-subscription-common"))
                implementation(project(":ok-subscription-stubs"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                api(kotlin("test-junit"))
            }
        }
    }
}