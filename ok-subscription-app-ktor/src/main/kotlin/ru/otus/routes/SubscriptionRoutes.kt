package ru.otus.routes

import SubscriptionProcessor
import io.ktor.server.application.*
import io.ktor.server.routing.*
import models.subscription.SubscriptionCommand
import ru.otus.otuskotlin.subscription.logging.common.mpLogger
import ru.otus.utils.processSubscriptionRq
import ru.otuskotlin.subscription.api.v1.models.*

private val loggerSubscription = mpLogger("subscriptions")
fun Route.subscriptionRouting(processor: SubscriptionProcessor) {
    route("/subscription") {
        post("/read") {
            call.processSubscriptionRq<SubscriptionReadRequest, SubscriptionReadResponse>(
                processor,
                loggerSubscription,
                "subscription-read",
                SubscriptionCommand.READ)
        }
        post("/search") {
            call.processSubscriptionRq<SubscriptionSearchRequest, SubscriptionSearchResponse>(
                processor,
                loggerSubscription,
                "subscription-search",
                SubscriptionCommand.SEARCH
            )
        }
        post("/pay") {
            call.processSubscriptionRq<SubscriptionPayRequest, SubscriptionPayResponse>(
                processor,
                loggerSubscription,
                "subscription-pay",
                SubscriptionCommand.PAY
            )
        }
    }
}