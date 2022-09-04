package contexts

import NONE
import kotlinx.datetime.Instant
import models.*

/**
 * Интерфейс базового контекста, хранящий общие свойства дочерних контекстов.
 */
interface BaseContext{
    var state: SbscrState
    var errors: MutableList<SbscrError>

    var workMode: SbscrWorkMode
    var stubCase: SbscrStubs

    var requestId: SbscrRequestId
    var timeStart: Instant //пока нигде не используется
}
