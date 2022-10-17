package ru.otus.plugins

import PlanProcessor
import SubscriptionProcessor
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import ru.otus.routes.planRouting
import ru.otus.routes.subscriptionRouting

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        val planProcessor = PlanProcessor()
        val subscriptionProcessor = SubscriptionProcessor()
        planRouting(planProcessor, subscriptionProcessor)
        subscriptionRouting(subscriptionProcessor)
    }
}
