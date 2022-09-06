rootProject.name = "ok-subscription-service-2206"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}
//include("ok-subscription-test-module")
include("ok-subscription-api-v1-jackson")
include("ok-subscription-api-v1-jackson")
include("ok-subscription-mappers-v1")
include("ok-subscription-common")
include("ok-subscription-app-ktor")