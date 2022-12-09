package repo.subscription

import models.SbscrDatePeriod
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.SubscriptionFilter

data class DbSubscriptionFilterRequest(
    val filter: SubscriptionFilter
) {
    constructor(
        planId: PlanId = PlanId.NONE,
        ownerId: SbscrUserId = SbscrUserId.NONE,
        isActive: Boolean? = null,
        startPeriod: SbscrDatePeriod? = null,
        endPeriod: SbscrDatePeriod? = null
    ) : this(
        SubscriptionFilter(
            planId = planId,
            ownerId = ownerId,
            isActive = isActive,
            boughtPeriod = startPeriod,
            expirationPeriod = endPeriod
        )
    )
}
