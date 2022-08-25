package toTransport

import contexts.SbscrContext
import exceptions.UnknownSbscrCommand
import models.*
import ru.otuskotlin.subscription.api.v1.models.*
import toTrasportErrors

/**
 * Маппер из внутренних моделей в транспортные
 * для  подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */
fun SbscrContext.toTransportSubscription(): IResponse = when (val cmd = command) {
    SbscrCommand.CREATE -> toTransportCreate()
    SbscrCommand.UPDATE -> toTransportUpdate()
    SbscrCommand.READ -> toTransportRead()
    SbscrCommand.READ_ALL -> toTransportReadAll()
    SbscrCommand.DELETE -> toTransportDelete()
    SbscrCommand.NONE -> throw UnknownSbscrCommand(cmd)
}

fun SbscrContext.toTransportCreate() = SubscriptionCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = sbscrResponse.toTransportSubscription()
)

fun SbscrContext.toTransportUpdate() = SubscriptionUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = sbscrResponse.toTransportSubscription()
)

fun SbscrContext.toTransportRead() = SubscriptionReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = sbscrResponse.toTransportSubscription()
)

fun SbscrContext.toTransportReadAll() = SubscriptionReadAllResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscriptions = sbscrResponses.toTransportSubscription()
)

fun SbscrContext.toTransportDelete() = SubscriptionDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == SbscrState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    subscription = sbscrResponse.toTransportSubscription()
)

private fun Subscription.toTransportSubscription(): SubscriptionResponseObject = SubscriptionResponseObject(
    id = id.takeIf { it != SbscrId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    duration = duration,
    price = price,
    conditions = conditions.takeIf { it.isNotEmpty() },
    visibility = visibility.toTransportVisibility(),
)



private fun List<Subscription>.toTransportSubscription() = this
    .map { it.toTransportSubscription() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun SbscrVisibility.toTransportVisibility(): SubscriptionVisibility? = when (this) {
    SbscrVisibility.PUBLIC -> SubscriptionVisibility.PUBLIC
    SbscrVisibility.ADMIN_ONLY -> SubscriptionVisibility.ADMIN_ONLY
    SbscrVisibility.NONE -> null
}
