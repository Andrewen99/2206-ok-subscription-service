package ru.otus.utils

import PlanProcessor
import SubscriptionProcessor
import contexts.PlanContext
import contexts.SubscriptionContext
import fromTransport
import helpers.asSbscrError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import models.plan.PlanCommand
import ru.otuskotlin.subscription.api.v1.models.IRequest
import ru.otuskotlin.subscription.api.v1.models.IResponse
import kotlinx.datetime.Clock
import models.SbscrState
import models.subscription.SubscriptionCommand
import toTransport.toTransportPlan
import toTransport.toTransportSubscription

suspend inline fun <reified Q: IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processPlanRq(
    processor: PlanProcessor,
    command: PlanCommand? = null
) {
    val ctx = PlanContext(
        timeStart = Clock.System.now()
    )
    try {
        ctx.principal = principal<JWTPrincipal>().toModel()
        val request = receive<Q>()
        ctx.fromTransport(request)
        processor.exec(ctx)
        respond(ctx.toTransportPlan())
    } catch (e: Throwable) {
        e.printStackTrace()
        command?.also { ctx.command = it }
        ctx.state = SbscrState.FAILING
        ctx.errors.add(e.asSbscrError())
        processor.exec(ctx)
        respond(ctx.toTransportPlan())
    }
}

suspend inline fun <reified Q: IRequest, @Suppress("unused") reified R:IResponse> ApplicationCall.processSubscriptionRq(
    processor: SubscriptionProcessor,
    command: SubscriptionCommand? = null
) {
    val ctx = SubscriptionContext(
        timeStart = Clock.System.now()
    )
    try {
        ctx.principal = principal<JWTPrincipal>().toModel()
        val request = receive<Q>()
        ctx.fromTransport(request)
        processor.exec(ctx)
        respond(ctx.toTransportSubscription())
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = SbscrState.FAILING
        ctx.errors.add(e.asSbscrError())
        processor.exec(ctx)
        respond(ctx.toTransportSubscription())
    }
}