package ru.otus

import PlanProcessor
import PostgresRepoFactory
import SubscriptionProcessor
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.RepoSettings
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionRepoSettings
import plan.PlanRepoInMemory
import ru.otus.plugins.*
import ru.otus.settings.KtorPostgresConfig
import subscription.SubscriptionRepoInMemory

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        val conf = HoconConfigLoader().load("./application.conf")
            ?: throw RuntimeException("Cannot read application.conf")
        config = conf
        connector {
            port =  8080
            host =  "0.0.0.0"
        }
        module {
            module()
        }
    }).apply{
        start(true)
    }
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module(
    repoSettings: RepoSettings? = null,
) {

    val repoSettings by lazy {
        if (repoSettings != null) {
            repoSettings
        } else {
            val postgresConfig = KtorPostgresConfig(environment)
            val planToSubscriptionRepo = PostgresRepoFactory.getTables(
                postgresConfig.url, postgresConfig.user, postgresConfig.password, postgresConfig.schema
            )
            RepoSettings(
                planRepoSettings = PlanRepoSettings(
                    repoTest = PlanRepoInMemory(),
                    repoProd = planToSubscriptionRepo.first
                ),
                subscriptionRepoSettings = SubscriptionRepoSettings(
                    repoTest = SubscriptionRepoInMemory(),
                    repoProd = planToSubscriptionRepo.second
                )
            )
        }
    }

    val planProcessor = PlanProcessor(repoSettings)
    val subscriptionProcessor = SubscriptionProcessor(repoSettings)

    configureSerialization()
    configureWebSocketsAndRestRouting(planProcessor, subscriptionProcessor)
}
