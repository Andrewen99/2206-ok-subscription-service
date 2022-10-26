package repo.subscription

import models.SbscrError
import models.subscription.Subscription
import repo.IDbResponse

data class DbSubscriptionsResponse(
    override val data: List<Subscription>?,
    override val success: Boolean,
    override val errors: List<SbscrError>
): IDbResponse<List<Subscription>>
