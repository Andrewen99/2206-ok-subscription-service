package repo.subscription

import models.subscription.SubscriptionFilter

data class DbSubscriptionFilterRequest(
    val filter: SubscriptionFilter
)
