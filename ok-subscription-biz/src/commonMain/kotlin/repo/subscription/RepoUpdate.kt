package repo.subscription

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.subscription.DbSubscriptionRequest


fun CorChainDsl<SubscriptionContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbSubscriptionRequest(subscriptionRepoPrepare)
        val result = subscriptionRepo.updateSubscription(request)
        val resultSubscription = result.data
        if (result.success && resultSubscription != null) {
            subscriptionRepoDone = resultSubscription
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
            resultSubscription?.let { subscriptionRepoDone = it }
        }
    }
}