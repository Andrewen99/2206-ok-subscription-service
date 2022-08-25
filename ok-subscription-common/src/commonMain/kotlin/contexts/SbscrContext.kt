package contexts

import NONE
import kotlinx.datetime.Instant
import models.*

/**
 * Контекст для подписок
 */
data class SbscrContext(
    var command: SbscrCommand = SbscrCommand.NONE,
    var sbscrRequest: Subscription = Subscription(),
    var sbscrResponse: Subscription = Subscription(),
    val sbscrResponses: MutableList<Subscription> = mutableListOf()
) : BaseContext()