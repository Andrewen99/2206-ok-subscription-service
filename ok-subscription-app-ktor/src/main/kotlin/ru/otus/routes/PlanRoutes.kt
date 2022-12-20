package ru.otus.routes

import PlanProcessor
import SubscriptionProcessor
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
import models.subscription.SubscriptionCommand
import ru.otus.otuskotlin.subscription.logging.common.mpLogger
import ru.otus.utils.processPlanRq
import ru.otus.utils.processSubscriptionRq
import ru.otuskotlin.subscription.api.v1.models.*

private val loggerPlan = mpLogger("plans")
fun Route.planRouting(processor: PlanProcessor, subscriptionProcessor: SubscriptionProcessor) {
    route("/plan") {
        post("/create") {
            call.processPlanRq<PlanCreateRequest, PlanCreateResponse>(processor, loggerPlan, "plan-create", PlanCommand.CREATE)
        }
        post("/update") {
            call.processPlanRq<PlanUpdateRequest, PlanUpdateResponse>(processor, loggerPlan, "plan-update", PlanCommand.UPDATE)
        }
        post("/read") {
            call.processPlanRq<PlanReadRequest, PlanReadResponse>(processor, loggerPlan, "plan-read", PlanCommand.READ)
        }
        post("/readAll") {
            call.processPlanRq<PlanReadAllRequest, PlanReadAllResponse>(processor, loggerPlan, "plan-read-all", PlanCommand.READ_ALL)
        }
        post("/delete") {
            call.processPlanRq<PlanDeleteRequest, PlanDeleteResponse>(processor, loggerPlan, "plan-delete", PlanCommand.DELETE)
        }
        post("/buy") {
            call.processSubscriptionRq<PlanBuyRequest, PlanBuyResponse>(subscriptionProcessor, loggerPlan, "plan-buy", SubscriptionCommand.BUY)
        }

    }

}