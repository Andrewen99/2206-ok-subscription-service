package models.subscription

import models.SbscrDatePeriod
import models.SbscrUserId

/**
 * Фильтр для поиска приобретенных подписок
 */
data class SubscriptionFilter(
    var ownerId: SbscrUserId = SbscrUserId.NONE,
    var boughtPeriod: SbscrDatePeriod? = null,
    var expirationPeriod: SbscrDatePeriod? = null,
    var isActive: Boolean? = null
)