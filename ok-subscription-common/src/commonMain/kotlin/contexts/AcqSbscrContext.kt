package contexts

import models.AcqSbscrCommand
import models.AcqSbscrFilter
import models.AcquiredSubscription
import models.SbscrId

/**
 * Контекст для приобретенных/приобретаемых подписок
 */
data class AcqSbscrContext(
    var command: AcqSbscrCommand = AcqSbscrCommand.NONE,
    var subscriptionId: SbscrId = SbscrId.NONE,
    var acqSbscrFilter: AcqSbscrFilter = AcqSbscrFilter(),
    var acqSbscrResponse: AcquiredSubscription = AcquiredSubscription(),
    val acqSbscrResponses: MutableList<AcquiredSubscription> = mutableListOf()
) : BaseContext()
