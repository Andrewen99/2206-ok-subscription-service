package repo.subscription

import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock

data class DbSubscriptionIdRequest(
    val id: SubscriptionId,
    val lock: SubscriptionLock
) {
    constructor(subscription: Subscription): this(subscription.id, subscription.lock)
}
