package repo.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun CorChainDsl<PlanContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные плана к удалению из БД"
    on { state == SbscrState.RUNNING }
    handle {
        planRepoPrepare = planValidating.deepCopy()
    }
}