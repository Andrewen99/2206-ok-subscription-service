package ru.otus.websocket

import PlanProcessor
import SubscriptionProcessor
import apiV1Mapper
import contexts.BaseContext
import contexts.PlanContext
import contexts.SubscriptionContext
import fromTransport
import helpers.asSbscrError
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import models.plan.PlanCommand
import models.subscription.SubscriptionCommand
import ru.otuskotlin.subscription.api.v1.models.IRequest
import toTransport.toTransportPlan
import toTransport.toTransportSubscription
import toTransport.toWsTransportInit

suspend fun DefaultWebSocketServerSession.planWsHandler(
    processor: PlanProcessor,
    sessions: MutableSet<KtorUserSession>
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)
    run {
        val ctx = PlanContext(
            timeStart = Clock.System.now()
        )
        // обработка запроса на инициализацию
        outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toWsTransportInit())))
    }
    incoming
        .receiveAsFlow()
        .mapNotNull { it as? Frame.Text }
        .map {
            frame ->
            val ctx = PlanContext(
                timeStart = Clock.System.now()
            )
            //обработка исключений без завершения flow
            try {
                val jsonStr = frame.readText()
                ctx.fromTransport(apiV1Mapper.readValue(jsonStr, IRequest::class.java))
                processor.exec(ctx)
                if (ctx.isUpdatableCommand()) {
                    sessions.forEach {
                        it.fwSession.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toTransportPlan())))
                    }
                } else {
                    outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toTransportPlan())))
                }
            } catch (_: ClosedReceiveChannelException) {
                sessions.remove(userSession)
            }catch (t: Throwable) {
                ctx.errors.add(t.asSbscrError())
                outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toWsTransportInit())))
            }
        }.collect()
}

suspend fun DefaultWebSocketServerSession.subscriptionWsHandler(
    processor: SubscriptionProcessor,
    sessions: MutableSet<KtorUserSession>
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)
    run {
        val ctx = SubscriptionContext(
            timeStart = Clock.System.now()
        )
        // обработка запроса на инициализацию
        outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toWsTransportInit())))
    }
    incoming
        .receiveAsFlow()
        .mapNotNull { it as? Frame.Text }
        .map {
                frame ->
            val ctx = SubscriptionContext(
                timeStart = Clock.System.now()
            )
            //обработка исключений без завершения flow
            try {
                val jsonStr = frame.readText()
                ctx.fromTransport(apiV1Mapper.readValue(jsonStr, IRequest::class.java))
                processor.exec(ctx)
                outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toTransportSubscription())))
            } catch (_: ClosedReceiveChannelException) {
                sessions.remove(userSession)
            }catch (t: Throwable) {
                ctx.errors.add(t.asSbscrError())
                outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(ctx.toWsTransportInit())))
            }
        }.collect()
}

private fun PlanContext.isUpdatableCommand() =
    this.command in listOf(PlanCommand.CREATE, PlanCommand.UPDATE, PlanCommand.DELETE)