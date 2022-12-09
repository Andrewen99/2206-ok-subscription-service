package subscription

import util.MIN_LOCAL_DATE
import kotlinx.datetime.LocalDate
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock

data class SubscriptionEntity(
    val id: String? = null,
    val ownerId: String? = null,
    val planId: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val isActive: Boolean = false,
    val lock: String? = null,
    val paymentStatus: String
) {

    constructor(model: Subscription) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        planId = model.planId.asString().takeIf { it.isNotBlank() },
        startDate = model.startDate,
        endDate = model.endDate,
        isActive = model.isActive,
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        paymentStatus = model.paymentStatus.name
    )

    fun toInternal() = Subscription(
        id = id?.let { SubscriptionId(it) } ?: SubscriptionId.NONE,
        ownerId =  ownerId?.let { SbscrUserId(it) } ?: SbscrUserId.NONE,
        planId = planId?.let { PlanId(it) } ?: PlanId.NONE,
        startDate = startDate ?: MIN_LOCAL_DATE,
        endDate = endDate ?: MIN_LOCAL_DATE,
        isActive = isActive,
        lock = lock?.let{ SubscriptionLock(it) } ?: SubscriptionLock.NONE,
        paymentStatus = SbscrPaymentStatus.valueOf(paymentStatus)
    )

}
