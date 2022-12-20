package toTransport

import contexts.PlanContext
import exceptions.UnknownSbscrCommand
import models.*
import models.plan.*
import permissions.PlanPermissionsClient
import ru.otuskotlin.subscription.api.v1.models.*
import toTrasportErrors

/**
 * Маппер из внутренних моделей в транспортные
 * для  подписки
 * Общие методы мапперов вынесены в [TransportUtil.kt]
 */
fun PlanContext.toTransportPlan(): IResponse = when (val cmd = command) {
    PlanCommand.CREATE -> toTransportCreate()
    PlanCommand.UPDATE -> toTransportUpdate()
    PlanCommand.READ -> toTransportRead()
    PlanCommand.READ_ALL -> toTransportReadAll()
    PlanCommand.DELETE -> toTransportDelete()
    PlanCommand.NONE -> throw UnknownSbscrCommand(cmd)
}

private fun PlanContext.toTransportCreate() = PlanCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state in listOf(SbscrState.RUNNING, SbscrState.FINISHING)) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    plan = planResponse.toTransportPlan(),
)

private fun PlanContext.toTransportUpdate() = PlanUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state in listOf(SbscrState.RUNNING, SbscrState.FINISHING)) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    plan = planResponse.toTransportPlan()
)

private fun PlanContext.toTransportRead() = PlanReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state in listOf(SbscrState.RUNNING, SbscrState.FINISHING)) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    plan = planResponse.toTransportPlan()
)

private fun PlanContext.toTransportReadAll() = PlanReadAllResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state in listOf(SbscrState.RUNNING, SbscrState.FINISHING)) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    plans = planResponses.toTransportPlan()
)

private fun PlanContext.toTransportDelete() = PlanDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state in listOf(SbscrState.RUNNING, SbscrState.FINISHING)) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors(),
    plan = planResponse.toTransportPlan()
)

private fun Plan.toTransportPlan(): PlanResponseObject = PlanResponseObject(
    id = id.takeIf { it != PlanId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    duration = duration,
    price = price,
    lock = lock.takeIf { it != PlanLock.NONE }?.asString(),
    conditions = conditions.takeIf { it.isNotEmpty() },
    visibility = visibility.toTransportVisibility(),
    permissions = permissionsClient.toTransportPermissions()
)

private fun Set<PlanPermissionsClient>.toTransportPermissions() : Set<PlanPermissions>? = this
    .map { it.toTransportPermission() }
    .toSet()
    .takeIf {  it.isNotEmpty() }

private fun PlanPermissionsClient.toTransportPermission() : PlanPermissions = when (this) {
    PlanPermissionsClient.READ -> PlanPermissions.READ
    PlanPermissionsClient.UPDATE -> PlanPermissions.UPDATE
    PlanPermissionsClient.DELETE -> PlanPermissions.DELETE
}



private fun List<Plan>.toTransportPlan() = this
    .map { it.toTransportPlan() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun SbscrPlanVisibility.toTransportVisibility(): PlanVisibility? = when (this) {
    SbscrPlanVisibility.PUBLIC -> PlanVisibility.PUBLIC
    SbscrPlanVisibility.ADMIN_ONLY -> PlanVisibility.ADMIN_ONLY
    SbscrPlanVisibility.NONE -> null
}
