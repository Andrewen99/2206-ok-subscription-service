package ru.otus.plugins

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
        planRouting()
        subscriptionRouting()
    }
}
