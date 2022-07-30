rootProject.name = "ok-subscription-service-2206"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion
    }
}
include("subscription-test-module")
