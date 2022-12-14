package ru.otus

import SqlTestCompanion.planAndSubscriptionRepoUnderTestContainer
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import io.ktor.client.plugins.websocket.*
import models.RepoSettings
import models.plan.Plan
import models.plan.PlanRepoSettings
import models.subscription.Subscription
import models.subscription.SubscriptionRepoSettings
import plan.PlanRepoInMemory
import ru.otus.settings.KtorAuthConfig
import subscription.SubscriptionRepoInMemory

const val UUID_OLD: String = "200_000_000"
const val UUID_NEW: String = "200_000_001"
fun ApplicationTestBuilder.myRestClient() = createClient {
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
}

fun ApplicationTestBuilder.initStubApp() = application {
    module(
        repoSettings = RepoSettings(),
        authConfig = KtorAuthConfig.TEST
    )
}

internal fun TestApplicationBuilder.initInMemoryApp(
    initPlans: List<Plan> = emptyList(),
    initSubscriptions: List<Subscription> = emptyList()
) = application {
    val planRepoSettings = PlanRepoSettings(repoTest = PlanRepoInMemory(initObjects = initPlans, randomUuid = { UUID_NEW }))

    val subscriptionRepoSettings = SubscriptionRepoSettings(
            repoTest = SubscriptionRepoInMemory(
                initObjects = initSubscriptions,
                randomUuid = { UUID_NEW })
        )

    val repoSettings = RepoSettings(
            planRepoSettings = planRepoSettings,
            subscriptionRepoSettings = subscriptionRepoSettings
        )

    module(repoSettings = repoSettings, authConfig = KtorAuthConfig.TEST)
}

internal fun TestApplicationBuilder.initPostgresApp(
    initPlans: List<Plan> = emptyList(),
    initSubscriptions: List<Subscription> = emptyList()
) = application {
    val planAndSubscriptionRepos = planAndSubscriptionRepoUnderTestContainer(
        initPlans,
        initSubscriptions,
        { UUID_NEW }
    )
    val planRepoSettings = PlanRepoSettings(repoTest = planAndSubscriptionRepos.first)
    val subscriptionRepoSettings = SubscriptionRepoSettings(repoTest = planAndSubscriptionRepos.second)
    val repoSettings = RepoSettings(
        planRepoSettings = planRepoSettings,
        subscriptionRepoSettings = subscriptionRepoSettings
    )
    module(repoSettings = repoSettings, authConfig = KtorAuthConfig.TEST)
}

fun ApplicationTestBuilder.myWsClient() = createClient {
    install(WebSockets)
}


