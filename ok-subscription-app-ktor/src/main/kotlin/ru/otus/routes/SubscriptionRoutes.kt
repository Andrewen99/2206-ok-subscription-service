package ru.otus.routes

import io.ktor.server.routing.*

fun Route.subscriptionRouting() {
    route("/subscription") {
        post("/read") {

        }
        post("/search") {

        }
        post("/pay") {

        }
    }
}