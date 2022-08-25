import contexts.AcqSbscrContext
import exceptions.UnknownRequestClass
import models.*
import ru.otuskotlin.subscription.api.v1.models.*
import java.time.LocalDate

/**
 * Маппер из транспорта во внутренние модели
 * для приобретенной подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */

fun AcqSbscrContext.fromTransport(request: IRequest) {
    when (request) {
        is SubscriptionBuyRequest -> fromTransport(request)
        is SubscriptionSearchAcquiredRequest -> fromTransport(request)
        else -> throw UnknownRequestClass(request.javaClass)
    }
    requestId = request.requestId()
}


fun AcqSbscrContext.fromTransport(request: SubscriptionBuyRequest) {
    command = AcqSbscrCommand.BUY
    subscriptionId = request.subscription?.id.toSbscrId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AcqSbscrContext.fromTransport(request: SubscriptionSearchAcquiredRequest) {
    command = AcqSbscrCommand.SEARCH_ACQUIRED
    acqSbscrFilter =  request.acquiredSubscriptionFilter?.toInternal() ?: AcqSbscrFilter()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SubscriptionSearchAcquiredFilter.toInternal(): AcqSbscrFilter = AcqSbscrFilter(
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