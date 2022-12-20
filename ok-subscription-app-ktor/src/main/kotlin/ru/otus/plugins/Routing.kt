package ru.otus.plugins

import PlanProcessor
import SubscriptionProcessor
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ru.otus.routes.planRouting
import ru.otus.routes.subscriptionRouting
import ru.otus.websocket.KtorPlanWsSessions
import ru.otus.websocket.KtorSubWsSessions
import ru.otus.websocket.planWsHandler
import ru.otus.websocket.subscriptionWsHandler

fun Application.configureWebSocketsAndRestRouting(
    planProcessor: PlanProcessor,
    subscriptionProcessor: SubscriptionProcessor
) {
    install(WebSockets)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authenticate("auth-jwt") {
            planRouting(planProcessor, subscriptionProcessor)
            subscriptionRouting(subscriptionProcessor)
        }

        webSocket("/plan") {
            planWsHandler(planProcessor, KtorPlanWsSessions.sessions)
        }

        webSocket("/subscription") {
            subscriptionWsHandler(subscriptionProcessor, KtorSubWsSessions.sessions)
        }


    }
}
