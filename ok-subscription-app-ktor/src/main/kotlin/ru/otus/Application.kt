package ru.otus

import PlanProcessor
import SubscriptionProcessor
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.RepoSettings
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionRepoSettings
import plan.PlanRepoInMemory
import ru.otus.plugins.*
import subscription.SubscriptionRepoInMemory

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(true)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module(
    repoSettings: RepoSettings? = null
) {
    val repoSettings by lazy {
        repoSettings ?: RepoSettings(
            planRepoSettings = PlanRepoSettings(repoTest = PlanRepoInMemory()),
            subscriptionRepoSettings = SubscriptionRepoSettings(repoTest = SubscriptionRepoInMemory())
        )
    }

    val planProcessor = PlanProcessor(repoSettings)
    val subscriptionProcessor = SubscriptionProcessor(repoSettings)

    configureSerialization()
    configureWebSocketsAndRestRouting(planProcessor, subscriptionProcessor)
}
