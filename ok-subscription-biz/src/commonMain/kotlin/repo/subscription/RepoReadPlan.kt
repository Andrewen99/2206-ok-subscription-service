package repo.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.plan.DbPlanIdRequest

fun CorChainDsl<SubscriptionContext>.repoReadPlan(title: String) = worker {
    this.title = title
    description = "Чтение плана из БД"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbPlanIdRequest(subscriptionRepoRead.planId)
        val result = planRepo.readPlan(request)
        val resultPlan = result.data
        if (result.success && resultPlan != null) {
            planRepoRead = resultPlan
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
    }
}