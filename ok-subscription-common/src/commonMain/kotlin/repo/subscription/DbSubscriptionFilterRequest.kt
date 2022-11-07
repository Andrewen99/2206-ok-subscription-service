package repo.subscription

import models.SbscrDatePeriod
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.SubscriptionFilter

data class DbSubscriptionFilterRequest(
    val filter: SubscriptionFilter
) {
    constructor(startPeriod: SbscrDatePeriod?, endPeriod: SbscrDatePeriod?) : this(
        SubscriptionFilter(
        boughtPeriod = startPeriod,
        expirationPeriod = endPeriod
    )
    )
    constructor(planId: PlanId) : this(
        SubscriptionFilter(planId = planId)
    )

    constructor(ownerId: SbscrUserId) : this(
        SubscriptionFilter(ownerId = ownerId)
    )

    constructor(isActive: Boolean) : this(
        SubscriptionFilter(isActive = isActive)
    )
}
