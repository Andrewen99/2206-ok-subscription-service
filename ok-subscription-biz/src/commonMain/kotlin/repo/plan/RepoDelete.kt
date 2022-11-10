package repo.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import repo.plan.DbPlanIdRequest

fun CorChainDsl<PlanContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление плана из БД"
    on { state == SbscrState.RUNNING }
    handle {
        val request = DbPlanIdRequest(planRepoPrepare)
        val result = planRepo.deletePlan(request)
        if (result.success) {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
        planRepoDone = planRepoRead
    }
}