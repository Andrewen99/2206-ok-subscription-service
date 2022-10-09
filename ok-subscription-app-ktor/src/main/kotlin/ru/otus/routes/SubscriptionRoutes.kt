package ru.otus.routes

import SubscriptionProcessor
import SubscriptionStubs
import contexts.SubscriptionContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.SbscrState
import models.subscription.SubscriptionCommand
import ru.otus.utils.processSubscriptionRq
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportSubscription

fun Route.subscriptionRouting(processor: SubscriptionProcessor) {
    route("/subscription") {
        post("/read") {
            call.processSubscriptionRq<SubscriptionReadRequest, SubscriptionReadResponse>(processor, SubscriptionCommand.READ)
        }
        post("/search") {
            call.processSubscriptionRq<SubscriptionSearchRequest, SubscriptionSearchResponse>(processor, SubscriptionCommand.SEARCH)
        }
        post("/pay") {
            call.processSubscriptionRq<SubscriptionPayRequest, SubscriptionPayResponse>(processor, SubscriptionCommand.PAY)
        }
    }
}