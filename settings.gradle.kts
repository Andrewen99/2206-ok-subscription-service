rootProject.name = "ok-subscription-service-2206"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val ktorVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
    }
}
include("ok-subscription-api-v1-jackson")
include("ok-subscription-api-v1-jackson")
include("ok-subscription-mappers-v1")
include("ok-subscription-common")
include("ok-subscription-app-ktor")
include("ok-subscription-stubs")
include("ok-subscription-cor")
include("ok-subscription-biz")
include("ok-subscription-repo-tests")
include("ok-subscription-repo-inmemory")
include("ok-subscription-repo-stub")
//include("ok-subscription-not-used-repo-gremlin")
include("ok-subscription-repo-postgresql")
include("ok-subscription-auth")
include("ok-subscription-logging")
include("ok-subscription-api-logs")
include("ok-subscription-logs-mapper")
include("ok-subscription-logging")
