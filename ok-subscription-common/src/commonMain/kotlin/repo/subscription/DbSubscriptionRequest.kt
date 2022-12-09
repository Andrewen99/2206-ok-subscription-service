package repo.subscription

import models.subscription.Subscription

data class DbSubscriptionRequest(
    val subscription: Subscription
)