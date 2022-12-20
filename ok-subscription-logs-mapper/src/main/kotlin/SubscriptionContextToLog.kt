import contexts.SubscriptionContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalDate
import models.SbscrRequestId
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionFilter
import models.subscription.SubscriptionId
import ru.otuskotlin.subscription.api.logs.models.*
import util.MIN_LOCAL_DATE

fun SubscriptionContext.toLog(logId:String ) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-subscription",
    subscriptionService = toSbscrLog(),
    errors = errors.map { it.toLog() }
)

fun SubscriptionContext.toSbscrLog(): SbscrLogModel? {
    val sbscrNone = Subscription()
    return SbscrLogModel(
        requestId = requestId.takeIf { it != SbscrRequestId.NONE}?.asString(),
        requestSubscription = subscriptionRequest.takeIf{ it != sbscrNone}?.toLog(),
        requestSubscriptionFilter = subscriptionFilter.takeIf{ it != SubscriptionFilter() }?.toLog(),
        responseSubscription = subscriptionResponse.takeIf { it != sbscrNone}?.toLog(),
        responseSubscriptions = subscriptionResponses.takeIf{ it.isNotEmpty()}?.filter{ it != sbscrNone}?.map{ it.toLog() },
    )
}

private fun Subscription.toLog() = SubscriptionLog(
    id = id.takeIf { it != SubscriptionId.NONE}?.asString(),
    ownerId = ownerId.takeIf { it != SbscrUserId.NONE }?.asString(),
    planId = planId.takeIf { it != PlanId.NONE }?.asString(),
    startDate = startDate.takeIf { it != MIN_LOCAL_DATE }?.toJavaLocalDate()?.format(DATE_FORMATTER),
    endDate = endDate.takeIf { it != MIN_LOCAL_DATE }?.toJavaLocalDate()?.format(DATE_FORMATTER),
    isActive = isActive,
    paymentStatus = paymentStatus.name
)

private fun SubscriptionFilter.toLog() = SubscriptionFilterLog(
    subscriptionId = subscriptionId.takeIf { it != SubscriptionId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != SbscrUserId.NONE }?.asString(),
    planId = planId.takeIf { it != PlanId.NONE }?.asString(),
    boughtPeriod = FromToDateObject(
        from = boughtPeriod?.startDate?.takeIf { it != MIN_LOCAL_DATE}?.toJavaLocalDate()?.format(DATE_FORMATTER),
        to = boughtPeriod?.endDate?.takeIf { it != MIN_LOCAL_DATE}?.toJavaLocalDate()?.format(DATE_FORMATTER),
    ),
    expirationPeriod = FromToDateObject(
        from = expirationPeriod?.startDate?.takeIf { it != MIN_LOCAL_DATE}?.toJavaLocalDate()?.format(DATE_FORMATTER),
        to = expirationPeriod?.endDate?.takeIf { it != MIN_LOCAL_DATE}?.toJavaLocalDate()?.format(DATE_FORMATTER),
    ),
    isActive = isActive
)