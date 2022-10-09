import models.*
import models.plan.Plan
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionId
import ru.otuskotlin.subscription.api.v1.models.*
import java.time.format.DateTimeFormatter

/**
 * Общие методы для мапперов
 */
internal fun String?.toPlanId() = this?.let { PlanId(it) } ?: PlanId.NONE
internal fun String?.toSbscrUserId() = this?.let { SbscrUserId(it) } ?: SbscrUserId.NONE
internal fun String?.toSubscriptionId() = this?.let { SubscriptionId(it) } ?: SubscriptionId.NONE
internal fun String?.toPlanWithId() = Plan(id = this.toPlanId())
internal fun String?.toSubscriptionWithId() = Subscription(id =this.toSubscriptionId())
internal fun IRequest?.requestId() = this?.requestId?.let { SbscrRequestId(it) } ?: SbscrRequestId.NONE

internal const val DATE_FORMAT: String = "dd.MM.yyyy"
internal val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

internal fun Debug?.transportToWorkMode(): SbscrWorkMode = when (this?.mode) {
    RequestDebugMode.PROD -> SbscrWorkMode.PROD
    RequestDebugMode.TEST -> SbscrWorkMode.TEST
    RequestDebugMode.STUB -> SbscrWorkMode.STUB
    null -> SbscrWorkMode.PROD
}

internal fun Debug?.transportToStubCase(): SbscrStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> SbscrStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> SbscrStubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> SbscrStubs.BAD_ID
    RequestDebugStubs.BAD_TITLE -> SbscrStubs.BAD_TITLE
    RequestDebugStubs.BAD_VISIBILITY -> SbscrStubs.BAD_VISIBILITY
    RequestDebugStubs.CANNOT_DELETE -> SbscrStubs.CANNOT_DELETE
    RequestDebugStubs.CANNOT_BUY -> SbscrStubs.CANNOT_BUY
    RequestDebugStubs.PAYMENT_ERROR -> SbscrStubs.PAYMENT_ERROR
    RequestDebugStubs.DB_ERROR -> SbscrStubs.DB_ERROR
    RequestDebugStubs.BAD_SEARCH_PARAMETERS -> SbscrStubs.BAD_SEARCH_PARAMETERS
    null -> SbscrStubs.NONE
}

internal fun List<SbscrError>.toTrasportErrors(): List<Error>? = this
    .map { it.toTrasportSubscription() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun SbscrError.toTrasportSubscription() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)