package toTransport

import DATE_FORMAT
import DATE_FORMATTER
import contexts.AcqSbscrContext
import contexts.SbscrContext
import exceptions.UnknownAcqSbscrCommand
import exceptions.UnknownSbscrCommand
import models.*
import ru.otuskotlin.subscription.api.v1.models.*
import toTrasportErrors
import java.time.LocalDate
/**
 * Маппер из внутренних моделей в транспортные
 * для приобретенной подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */

fun AcqSbscrContext.toTransportSubscription(): IResponse = when (val cmd = command) {
    AcqSbscrCommand.BUY -> toTransportBuy()
    AcqSbscrCommand.SEARCH_ACQUIRED -> toTransportSearch()
    AcqSbscrCommand.NONE -> throw UnknownAcqSbscrCommand(cmd)
}

fun AcqSbscrContext.toTransportBuy(): SubscriptionBuyResponse = SubscriptionBuyResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    acquiredSubscription = acqSbscrResponse.toTransportAcqSbscr()
)

fun AcqSbscrContext.toTransportSearch(): SubscriptionSearchAcquiredResponse = SubscriptionSearchAcquiredResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    acquiredSubscriptions = acqSbscrResponses.toTransportAcqSbscr()
)

private fun List<AcquiredSubscription>.toTransportAcqSbscr() : List<AcquiredSubscriptionResponseObject>? = this
    .map { it.toTransportAcqSbscr() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AcquiredSubscription.toTransportAcqSbscr(): AcquiredSubscriptionResponseObject =
    AcquiredSubscriptionResponseObject(
        id = id.takeIf { it != AcqSbscrId.NONE }?.asString(),
        subscriptionId = subscriptionId.takeIf { it != SbscrId.NONE }?.asString(),
        startDate = startDate.takeIf { it != LocalDate.MIN }?.format(DATE_FORMATTER),
        endDate = endDate.takeIf { it != LocalDate.MIN }?.format(DATE_FORMATTER),
        isActive = isActive
    )