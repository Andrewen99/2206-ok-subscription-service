package toTransport

import DATE_FORMATTER
import contexts.SubscriptionContext
import exceptions.UnknownAcqSbscrCommand
import models.*
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import ru.otuskotlin.subscription.api.v1.models.*
import toTrasportErrors
import java.time.LocalDate
/**
 * Маппер из внутренних моделей в транспортные
 * для приобретенной подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */

fun SubscriptionContext.toTransportSubscription(): IResponse = when (val cmd = command) {
    SubscriptionCommand.BUY -> toTransportBuy()
    SubscriptionCommand.SEARCH -> toTransportSearch()
    SubscriptionCommand.READ -> toTransportRead()
    SubscriptionCommand.PAY -> toTransportPay()
    SubscriptionCommand.NONE -> throw UnknownAcqSbscrCommand(cmd)
}

private fun SubscriptionContext.toTransportBuy(): PlanBuyResponse = PlanBuyResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = subscriptionResponse.toTransportSubscription()
)

private fun SubscriptionContext.toTransportSearch(): SubscriptionSearchResponse = SubscriptionSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscriptions = subscriptionResponses.toTransportSubscription()
)

private fun SubscriptionContext.toTransportRead(): SubscriptionReadResponse = SubscriptionReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = subscriptionResponse.toTransportSubscription()
)

private fun SubscriptionContext.toTransportPay(): SubscriptionPayResponse = SubscriptionPayResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = subscriptionResponse.toTransportSubscription()
)

private fun List<Subscription>.toTransportSubscription() : List<SubscriptionResponseObject>? = this
    .map { it.toTransportSubscription() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Subscription.toTransportSubscription(): SubscriptionResponseObject =
    SubscriptionResponseObject(
        id = id.takeIf { it != SubscriptionId.NONE }?.asString(),
        planId = planId.takeIf { it != PlanId.NONE }?.asString(),
        startDate = startDate.takeIf { it != LocalDate.MIN }?.format(DATE_FORMATTER),
        endDate = endDate.takeIf { it != LocalDate.MIN }?.format(DATE_FORMATTER),
        isActive = isActive,
        paymentStatus = paymentStatus.toTransportPaymentStatus()
    )

private fun SbscrPaymentStatus.toTransportPaymentStatus(): SubscriptionResponseObject.PaymentStatus = when (this) {
    SbscrPaymentStatus.NOT_PAYED -> SubscriptionResponseObject.PaymentStatus.NOT_PAID
    SbscrPaymentStatus.PAYMENT_IN_PROGRESS -> SubscriptionResponseObject.PaymentStatus.PAYMENT_IN_PROGRESS
    SbscrPaymentStatus.PAYED -> SubscriptionResponseObject.PaymentStatus.PAID
}