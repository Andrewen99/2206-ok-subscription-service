import contexts.PlanContext
import exceptions.UnknownRequestClass
import models.*
import models.plan.Plan
import models.plan.PlanCommand
import models.plan.SbscrPlanVisibility
import ru.otuskotlin.subscription.api.v1.models.*

/**
 * Маппер из транспорта во внутренние модели
 * для подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */
fun PlanContext.fromTransport(request: IRequest) {
    when (request) {
        is PlanCreateRequest -> fromTransport(request)
        is PlanUpdateRequest -> fromTransport(request)
        is PlanReadRequest -> fromTransport(request)
        is PlanReadAllRequest -> fromTransport(request)
        is PlanDeleteRequest -> fromTransport(request)
        else -> throw UnknownRequestClass(request.javaClass)
    }
    requestId = request.requestId()
}

fun PlanContext.fromTransport(request: PlanCreateRequest) {
    command = PlanCommand.CREATE
    planRequest = request.plan?.toInternal() ?: Plan()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PlanContext.fromTransport(request: PlanUpdateRequest) {
    command = PlanCommand.UPDATE
    planRequest = request.plan?.toInternal() ?: Plan()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PlanContext.fromTransport(request: PlanReadRequest) {
    command = PlanCommand.READ
    planRequest = request.plan?.id.toPlanWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PlanContext.fromTransport(request: PlanReadAllRequest) {
    command = PlanCommand.READ_ALL
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PlanContext.fromTransport(request: PlanDeleteRequest) {
    command = PlanCommand.READ_ALL
    planRequest = request.plan?.id.toPlanWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PlanCreateObject.toInternal(): Plan = Plan(
    title = this.title ?: "",
    conditions = this.conditions?.toMutableSet() ?: mutableSetOf(),
    duration = this.duration ?: 0,
    price = this.price ?: "0",
    visibility = this.visibility.fromTransport()
)

private fun PlanUpdateObject.toInternal(): Plan = Plan(
    id = this.id.toPlanId(),
    title = this.title ?: "",
    conditions = this.conditions?.toMutableSet() ?: mutableSetOf(),
    duration = this.duration ?: 0,
    price = this.price ?: "0",
    visibility = this.visibility.fromTransport()
)

private fun PlanVisibility?.fromTransport(): SbscrPlanVisibility = when (this) {
    PlanVisibility.ADMIN_ONLY -> SbscrPlanVisibility.ADMIN_ONLY
    PlanVisibility.PUBLIC -> SbscrPlanVisibility.PUBLIC
    null -> SbscrPlanVisibility.NONE
}