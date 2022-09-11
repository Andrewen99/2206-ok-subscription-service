package ru.otus.routes

import SubscriptionStubs
import contexts.PlanContext
import contexts.SubscriptionContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportPlan
import toTransport.toTransportSubscription

fun Route.planRouting() {
    route("/plan") {
        post("/create") {
            val request = call.receive<PlanCreateRequest>()
            val context = PlanContext()
            context.fromTransport(request)
            //business logic
            context.planResponse = PlanStubs.get()
            call.respond(context.toTransportPlan())
        }
        post("/update") {
            val request = call.receive<PlanUpdateRequest>()
            val context = PlanContext()
            context.fromTransport(request)
            //business logic
            context.planResponse = PlanStubs.get()
            call.respond(context.toTransportPlan())
        }
        post("/read") {
            val request = call.receive<PlanReadRequest>()
            val context = PlanContext()
            context.fromTransport(request)
            //business logic
            context.planResponse = PlanStubs.get()
            call.respond(context.toTransportPlan())
        }
        post("/readAll") {
            val request = call.receive<PlanReadAllRequest>()
            val context = PlanContext()
            context.fromTransport(request)
            //business logic
            context.planResponses += PlanStubs.getAll()
            call.respond(context.toTransportPlan())
        }
        post("/delete") {
            val request = call.receive<PlanDeleteRequest>()
            val context = PlanContext()
            context.fromTransport(request)
            //business logic
            context.planResponse = PlanStubs.get()
            call.respond(context.toTransportPlan())
        }
        post("/buy") {
            val request = call.receive<PlanBuyRequest>()
            val context = SubscriptionContext()
            context.fromTransport(request)
            //business logic
            context.subscriptionResponse = SubscriptionStubs.get()
            call.respond(context.toTransportSubscription())
        }

    }

}