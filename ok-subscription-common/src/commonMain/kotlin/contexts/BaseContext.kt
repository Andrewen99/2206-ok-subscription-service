package contexts

import NONE
import kotlinx.datetime.Instant
import models.*

/**
 * Базовый контекст, объединяющий общие свойства.
 */
abstract class BaseContext(
    var state: SbscrState = SbscrState.NONE,
    var errors: MutableList<SbscrError> = mutableListOf(),

    var workMode: SbscrWorkMode = SbscrWorkMode.PROD,
    var stubCase: SbscrStubs = SbscrStubs.NONE,

    var requestId: SbscrRequestId = SbscrRequestId.NONE,
    var timeStart: Instant = Instant.NONE //пока нигде не используется
)
