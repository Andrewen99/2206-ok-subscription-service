package models.subscription

import models.plan.PlanId
import java.time.LocalDate

/**
 * Приобретенная подписка
 */
data class Subscription(
    var id: SubscriptionId = SubscriptionId.NONE,
    var planId: PlanId = PlanId.NONE,
    var startDate: LocalDate = LocalDate.MIN,
    var endDate: LocalDate = LocalDate.MIN,
    var isActive: Boolean = false,
    var paymentStatus: SbscrPaymentStatus = SbscrPaymentStatus.NOT_PAYED
)