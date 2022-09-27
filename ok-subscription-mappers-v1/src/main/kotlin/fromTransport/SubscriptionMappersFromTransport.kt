import contexts.SubscriptionContext
import exceptions.UnknownRequestClass
import models.*
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionFilter
import ru.otuskotlin.subscription.api.v1.models.*
import java.time.LocalDate

/**
 * Маппер из транспорта во внутренние модели
 * для приобретенной подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */

fun SubscriptionContext.fromTransport(request: IRequest) {
    when (request) {
        is PlanBuyRequest -> fromTransport(request)
        is SubscriptionSearchRequest -> fromTransport(request)
        is SubscriptionReadRequest -> fromTransport(request)
        is SubscriptionPayRequest -> fromTransport(request)
        else -> throw UnknownRequestClass(request.javaClass)
    }
}


fun SubscriptionContext.fromTransport(request: PlanBuyRequest) {
    command = SubscriptionCommand.BUY
    requestId = request.requestId()
    planId = request.plan?.id.toPlanId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SubscriptionContext.fromTransport(request: SubscriptionSearchRequest) {
    command = SubscriptionCommand.SEARCH
    requestId = request.requestId()
    subscriptionFilter =  request.subscriptionFilter?.toInternal() ?: SubscriptionFilter()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SubscriptionContext.fromTransport(request: SubscriptionReadRequest) {
    command = SubscriptionCommand.READ
    requestId = request.requestId()
    subscriptionRequest = request.subscription?.id.toSubscriptionWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SubscriptionContext.fromTransport(request: SubscriptionPayRequest) {
    command = SubscriptionCommand.PAY
    requestId = request.requestId()
    subscriptionRequest = request.subscription?.id.toSubscriptionWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SubscriptionSearchFilter.toInternal(): SubscriptionFilter = SubscriptionFilter(
    ownerId = this.ownerId.toSbscrUserId(),
    boughtPeriod = this.boughtPeriod?.toInternalOrNull(),
    expirationPeriod = this.boughtPeriod?.toInternalOrNull(),
    isActive = this.isActive
)

private fun FromToDateObject.toInternalOrNull(): SbscrDatePeriod? {
    if (!this.from.isNullOrBlank() && !this.to.isNullOrBlank()) {
        return SbscrDatePeriod(
            LocalDate.parse(this.from, DATE_FORMATTER),
            LocalDate.parse(this.to, DATE_FORMATTER)
        )
    }
    return null
}