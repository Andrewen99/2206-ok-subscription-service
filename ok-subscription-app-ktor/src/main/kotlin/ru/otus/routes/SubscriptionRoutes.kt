package ru.otus.routes

import SubscriptionStubs
import contexts.SubscriptionContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otuskotlin.subscription.api.v1.models.SubscriptionPayRequest
import ru.otuskotlin.subscription.api.v1.models.SubscriptionReadRequest
import ru.otuskotlin.subscription.api.v1.models.SubscriptionSearchRequest
import toTransport.toTransportSubscription

fun Route.subscriptionRouting() {
    route("/subscription") {
        post("/read") {
            val request = call.receive<SubscriptionReadRequest>()
            val context = SubscriptionContext()
            context.fromTransport(request)
            //business logic
            context.subscriptionResponse = SubscriptionStubs.get()
            call.respond(context.toTransportSubscription())
        }
        post("/search") {
            val request = call.receive<SubscriptionSearchRequest>()
            val context = SubscriptionContext()
            context.fromTransport(request)
            //business logic
            context.subscriptionResponses += SubscriptionStubs.getAll()
            call.respond(context.toTransportSubscription())
        }
        post("/pay") {
            val request = call.receive<SubscriptionPayRequest>()
            val context = SubscriptionContext()
            context.fromTransport(request)
            //business logic
            context.subscriptionResponse = SubscriptionStubs.get()
            call.respond(context.toTransportSubscription())
        }
    }
}