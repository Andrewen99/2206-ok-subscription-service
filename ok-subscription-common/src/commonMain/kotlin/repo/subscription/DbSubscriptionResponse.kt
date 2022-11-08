package repo.subscription

import models.SbscrError
import models.subscription.Subscription
import repo.IDbResponse

data class DbSubscriptionResponse(
    override val data: Subscription?,
    override val success: Boolean,
    override val errors: List<SbscrError> = emptyList()
) : IDbResponse<Subscription>
