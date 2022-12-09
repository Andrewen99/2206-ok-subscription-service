package repo.plan

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.subscription.DbSubscriptionRequest

fun CorChainDsl<PlanContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbPlanRequest(planRepoPrepare)
        val result = planRepo.updatePlan(request)
        val resultPlan = result.data
        if (result.success && resultPlan != null) {
            planRepoDone = resultPlan
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
            resultPlan?.let { planRepoDone = it }
        }
    }
}