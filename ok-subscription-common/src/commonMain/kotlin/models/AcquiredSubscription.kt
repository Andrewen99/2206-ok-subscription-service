package models

import java.time.LocalDate

/**
 * Приобретенная подписка
 */
data class AcquiredSubscription(
    var id: AcqSbscrId = AcqSbscrId.NONE,
    var subscriptionId: SbscrId = SbscrId.NONE,
    var startDate: LocalDate = LocalDate.MIN,
    var endDate: LocalDate = LocalDate.MIN,
    var isActive: Boolean = false
)