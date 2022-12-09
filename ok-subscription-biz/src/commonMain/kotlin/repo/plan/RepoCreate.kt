package repo.plan

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.plan.DbPlanRequest
import repo.subscription.DbSubscriptionRequest

fun CorChainDsl<PlanContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление плана в БД"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbPlanRequest(planRepoPrepare)
        val result = planRepo.createPlan(request)
        val resultPlan = result.data
        if (result.success && resultPlan != null) {
            planRepoDone = resultPlan
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
    }
}