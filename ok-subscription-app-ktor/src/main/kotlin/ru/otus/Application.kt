package ru.otus

import PlanProcessor
import PostgresRepoFactory
import SubscriptionProcessor
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.RepoSettings
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionRepoSettings
import plan.PlanRepoInMemory
import ru.otus.plugins.*
import ru.otus.settings.*
import ru.otus.settings.KtorAuthConfig.Companion.GROUPS_CLAIM
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
    authConfig: KtorAuthConfig = createAuthConfigFromEnvironment(environment)
) {

    val repoSettings by lazy {
        if (repoSettings != null) {
            repoSettings
        } else {
            val postgresConfig = createPostgresConfigFromEnvironment(environment)
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

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm

            verifier {
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    val planProcessor = PlanProcessor(repoSettings)
    val subscriptionProcessor = SubscriptionProcessor(repoSettings)

    configureSerialization()
    configureWebSocketsAndRestRouting(planProcessor, subscriptionProcessor)
}
