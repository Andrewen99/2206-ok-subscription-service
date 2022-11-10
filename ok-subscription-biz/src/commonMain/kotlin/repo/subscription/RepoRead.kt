package repo.subscription

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.plan.DbPlanIdRequest
import repo.subscription.DbSubscriptionIdRequest


fun CorChainDsl<SubscriptionContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение плана из БД"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbSubscriptionIdRequest(subscriptionValidated)
        val result = subscriptionRepo.readSubscription(request)
        val resultSubscription = result.data
        if (result.success && resultSubscription != null) {
            subscriptionRepoRead = resultSubscription
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
    }
}
