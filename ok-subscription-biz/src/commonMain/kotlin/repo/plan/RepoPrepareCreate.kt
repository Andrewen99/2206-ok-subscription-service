package repo.plan

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun CorChainDsl<PlanContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в БД"
    on { state == SbscrState.RUNNING }
    handle {
        planRepoPrepare = planValidated.deepCopy()
    }
}