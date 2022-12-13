package models.subscription

import models.SbscrDatePeriod
import models.SbscrUserId
import models.plan.PlanId

/**
 * Фильтр для поиска приобретенных подписок
 */
data class SubscriptionFilter(
    var ownerId: SbscrUserId = SbscrUserId.NONE,
    var planId: PlanId = PlanId.NONE,
    var subscriptionId: SubscriptionId = SubscriptionId.NONE,
    var boughtPeriod: SbscrDatePeriod? = null,
    var expirationPeriod: SbscrDatePeriod? = null,
    var isActive: Boolean? = null,
    var searchPermissions: MutableSet<SearchPermissions> = mutableSetOf()
) {
    fun deepCopy() = copy(
        boughtPeriod = boughtPeriod?.copy(),
        expirationPeriod = expirationPeriod?.copy()
    )
}