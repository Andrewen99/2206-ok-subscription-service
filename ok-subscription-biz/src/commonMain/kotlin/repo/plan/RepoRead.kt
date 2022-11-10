package repo.plan

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.plan.DbPlanIdRequest
import repo.subscription.DbSubscriptionIdRequest

fun CorChainDsl<PlanContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение плана из БД"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbPlanIdRequest(planValidated)
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
