import contexts.SbscrContext
import exceptions.UnknownRequestClass
import models.*
import ru.otuskotlin.subscription.api.v1.models.*

/**
 * Маппер из транспорта во внутренние модели
 * для подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */
fun SbscrContext.fromTransport(request: IRequest) {
    when (request) {
        is SubscriptionCreateRequest -> fromTransport(request)
        is SubscriptionUpdateRequest -> fromTransport(request)
        is SubscriptionReadRequest -> fromTransport(request)
        is SubscriptionReadAllRequest -> fromTransport(request)
        is SubscriptionDeleteRequest -> fromTransport(request)
        else -> throw UnknownRequestClass(request.javaClass)
    }
    requestId = request.requestId()
}

fun SbscrContext.fromTransport(request: SubscriptionCreateRequest) {
    command = SbscrCommand.CREATE
    sbscrRequest = request.subscription?.toInternal() ?: Subscription()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SbscrContext.fromTransport(request: SubscriptionUpdateRequest) {
    command = SbscrCommand.UPDATE
    sbscrRequest = request.subscription?.toInternal() ?: Subscription()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SbscrContext.fromTransport(request: SubscriptionReadRequest) {
    command = SbscrCommand.READ
    sbscrRequest = request.subscription?.id.toSbscrWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SbscrContext.fromTransport(request: SubscriptionReadAllRequest) {
    command = SbscrCommand.READ_ALL
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun SbscrContext.fromTransport(request: SubscriptionDeleteRequest) {
    command = SbscrCommand.READ_ALL
    sbscrRequest = request.subscription?.id.toSbscrWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SubscriptionCreateObject.toInternal(): Subscription = Subscription(
    title = this.title ?: "",
    conditions = this.conditions?.toMutableSet() ?: mutableSetOf(),
    duration = this.duration ?: 0,
    price = this.price ?: "0",
    visibility = this.visibility.fromTransport()
)

private fun SubscriptionUpdateObject.toInternal(): Subscription = Subscription(
    id = this.id.toSbscrId(),
    title = this.title ?: "",
    conditions = this.conditions?.toMutableSet() ?: mutableSetOf(),
    duration = this.duration ?: 0,
    price = this.price ?: "0",
    visibility = this.visibility.fromTransport()
)

private fun SubscriptionVisibility?.fromTransport(): SbscrVisibility = when (this) {
    SubscriptionVisibility.ADMIN_ONLY -> SbscrVisibility.ADMIN_ONLY
    SubscriptionVisibility.PUBLIC -> SbscrVisibility.PUBLIC
    null -> SbscrVisibility.NONE
}