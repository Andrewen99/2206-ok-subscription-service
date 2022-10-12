package models.subscription

import MIN_LOCAL_DATE
import kotlinx.datetime.LocalDate
import models.SbscrUserId
import models.plan.PlanId

/**
 * Приобретенная подписка
 */
data class Subscription(
    var id: SubscriptionId = SubscriptionId.NONE,
    var ownerId: SbscrUserId = SbscrUserId.NONE,
    var planId: PlanId = PlanId.NONE,
    var startDate: LocalDate = MIN_LOCAL_DATE,
    var endDate: LocalDate = MIN_LOCAL_DATE,
    var isActive: Boolean = false,
    var paymentStatus: SbscrPaymentStatus = SbscrPaymentStatus.NOT_PAYED
) {
    fun deepCopy() = copy()
}