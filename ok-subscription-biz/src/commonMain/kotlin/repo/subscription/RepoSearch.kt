package repo.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrUserId
import models.plan.PlanId

fun CorChainDsl<SubscriptionContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbSubscriptionFilterRequest(
            planId = subscriptionFilterValidated.planId,
            ownerId = subscriptionFilterValidated.ownerId,
            isActive = subscriptionFilterValidated.isActive,
            startPeriod = subscriptionFilterValidated.boughtPeriod,
            endPeriod = subscriptionFilterValidated.expirationPeriod
        )
        val result = subscriptionRepo.searchSubscription(request)
        val resultSubscriptions = result.data
        if (result.success && resultSubscriptions != null) {
            subscriptionsRepoDone = resultSubscriptions.toMutableList()
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
    }
}