package ru.otus.routes

import PlanProcessor
import SubscriptionStubs
import contexts.PlanContext
import contexts.SubscriptionContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.SbscrState
import models.plan.PlanCommand
import ru.otus.utils.processPlanRq
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportPlan
import toTransport.toTransportSubscription

fun Route.planRouting(processor: PlanProcessor) {
    route("/plan") {
        post("/create") {
            call.processPlanRq<PlanCreateRequest, PlanCreateResponse>(processor, PlanCommand.CREATE)
        }
        post("/update") {
            call.processPlanRq<PlanUpdateRequest, PlanUpdateResponse>(processor, PlanCommand.UPDATE)
        }
        post("/read") {
            call.processPlanRq<PlanReadRequest, PlanReadResponse>(processor, PlanCommand.READ)
        }
        post("/readAll") {
            call.processPlanRq<PlanReadAllRequest, PlanReadAllResponse>(processor, PlanCommand.READ_ALL)
        }
        post("/delete") {
            call.processPlanRq<PlanDeleteRequest, PlanDeleteResponse>(processor, PlanCommand.DELETE)
        }
        post("/buy") {
            val request = call.receive<PlanBuyRequest>()
            val context = SubscriptionContext(state = SbscrState.RUNNING)
            context.fromTransport(request)
            //business logic
            context.subscriptionResponse = SubscriptionStubs.SUBSCRIPTION1
            call.respond(context.toTransportSubscription())
        }

    }

}