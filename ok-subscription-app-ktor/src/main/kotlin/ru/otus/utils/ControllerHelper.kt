package ru.otus.utils

import PlanProcessor
import contexts.PlanContext
import fromTransport
import helpers.asSbscrError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import models.plan.PlanCommand
import ru.otuskotlin.subscription.api.v1.models.IRequest
import ru.otuskotlin.subscription.api.v1.models.IResponse
import kotlinx.datetime.Clock
import models.SbscrState
import toTransport.toTransportPlan

suspend inline fun <reified Q: IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processPlanRq(
    processor: PlanProcessor,
    command: PlanCommand? = null
) {
    val ctx = PlanContext(
        timeStart = Clock.System.now()
    )
    try {
        val request = receive<Q>()
        ctx.fromTransport(request)
        processor.exec(ctx)
        respond(ctx.toTransportPlan())
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = SbscrState.FAILING
        ctx.errors.add(e.asSbscrError())
        processor.exec(ctx)
        respond(ctx.toTransportPlan())
    }
}