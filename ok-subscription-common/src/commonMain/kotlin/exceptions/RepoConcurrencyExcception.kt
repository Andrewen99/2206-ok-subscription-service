package exceptions

import models.plan.PlanLock
import models.subscription.SubscriptionLock

class RepoConcurrencyException(
    expectedLock: String,
    actualLock: String?
) : RuntimeException("Expected lock is $expectedLock while actual lock in db is ${actualLock ?: ""}")