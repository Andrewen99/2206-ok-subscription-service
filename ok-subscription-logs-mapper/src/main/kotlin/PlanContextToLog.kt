import contexts.PlanContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalDate
import models.SbscrRequestId
import models.SbscrUserId
import models.plan.Plan
import models.plan.PlanId
import ru.otuskotlin.subscription.api.logs.models.CommonLogModel
import ru.otuskotlin.subscription.api.logs.models.PlanLog
import ru.otuskotlin.subscription.api.logs.models.SbscrLogModel

fun PlanContext.toLog(logId:String ) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-plan",
    subscriptionService = toSbscrLog(),
    errors = errors.map { it.toLog() }
)

fun PlanContext.toSbscrLog(): SbscrLogModel? {
    val sbscrNone = Plan()
    return SbscrLogModel(
        requestId = requestId.takeIf { it != SbscrRequestId.NONE}?.asString(),
        requestPlan = planRequest.takeIf{ it != sbscrNone}?.toLog(),
        responsePlan = planResponse.takeIf { it != sbscrNone}?.toLog(),
        responsePlans = planResponses.takeIf{ it.isNotEmpty()}?.filter{ it != sbscrNone}?.map{ it.toLog() },
    )
}

private fun Plan.toLog() = PlanLog(
    id = id.takeIf { it != PlanId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    conditions = conditions.takeIf { it.isNotEmpty() },
    duration = duration,
    price = price,
    visibility = visibility.name,
    )
