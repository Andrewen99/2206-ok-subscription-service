import models.*
import ru.otuskotlin.subscription.api.v1.models.Error
import ru.otuskotlin.subscription.api.v1.models.IRequest
import ru.otuskotlin.subscription.api.v1.models.SubscriptionDebug
import ru.otuskotlin.subscription.api.v1.models.SubscriptionRequestDebugMode
import ru.otuskotlin.subscription.api.v1.models.SubscriptionRequestDebugStubs
import java.time.format.DateTimeFormatter

/**
 * Общие методы для мапперов
 */
internal fun String?.toSbscrId() = this?.let { SbscrId(it) } ?: SbscrId.NONE
internal fun String?.toSbscrUserId() = this?.let { SbscrUserId(it) } ?: SbscrUserId.NONE
internal fun String?.toSbscrWithId() = Subscription(id = this.toSbscrId())
internal fun IRequest?.requestId() = this?.requestId?.let { SbscrRequestId(it) } ?: SbscrRequestId.NONE

internal const val DATE_FORMAT: String = "dd.MM.yyyy"
internal val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

internal fun SubscriptionDebug?.transportToWorkMode(): SbscrWorkMode = when (this?.mode) {
    SubscriptionRequestDebugMode.PROD -> SbscrWorkMode.PROD
    SubscriptionRequestDebugMode.TEST -> SbscrWorkMode.TEST
    SubscriptionRequestDebugMode.STUB -> SbscrWorkMode.STUB
    null -> SbscrWorkMode.PROD
}

internal fun SubscriptionDebug?.transportToStubCase(): SbscrStubs = when (this?.stub) {
    SubscriptionRequestDebugStubs.SUCCESS -> SbscrStubs.SUCCESS
    SubscriptionRequestDebugStubs.NOT_FOUND -> SbscrStubs.NOT_FOUND
    SubscriptionRequestDebugStubs.BAD_ID -> SbscrStubs.BAD_ID
    SubscriptionRequestDebugStubs.BAD_TITLE -> SbscrStubs.BAD_TITLE
    SubscriptionRequestDebugStubs.BAD_VISIBILITY -> SbscrStubs.BAD_VISIBILITY
    SubscriptionRequestDebugStubs.CANNOT_DELETE -> SbscrStubs.CANNOT_DELETE
    SubscriptionRequestDebugStubs.CANNOT_BUY -> SbscrStubs.CANNOT_BUY
    SubscriptionRequestDebugStubs.BAD_SEARCH_STRING -> SbscrStubs.BAD_SEARCH_STRING
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