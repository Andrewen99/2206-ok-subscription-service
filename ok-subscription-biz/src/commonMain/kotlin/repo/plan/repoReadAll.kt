package repo.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun CorChainDsl<PlanContext>.repoReadAll(title: String) = worker {
    this.title = title
    description = "Чтение всех планов из БД"
    on { state == SbscrState.RUNNING }
    handle {
        val result = planRepo.readAllPlans()
        val resultPlans = result.data
        if (result.success) {
            planRepoReadAll = resultPlans?.toMutableList() ?: mutableListOf()
        } else {
            state = SbscrState.FAILING
            errors.addAll(result.errors)
        }
    }
}