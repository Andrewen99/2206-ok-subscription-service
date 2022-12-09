package models

import models.plan.PlanRepoSettings
import models.subscription.SubscriptionRepoSettings

class RepoSettings(
    val planRepoSettings: PlanRepoSettings = PlanRepoSettings(),
    val subscriptionRepoSettings: SubscriptionRepoSettings = SubscriptionRepoSettings()
)