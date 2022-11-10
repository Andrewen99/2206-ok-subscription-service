package repo.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun CorChainDsl<PlanContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем даные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == SbscrState.RUNNING }
    handle {
        planRepoPrepare = planRepoRead.deepCopy().apply {
            this.title = planValidated.title
            conditions = planValidated.conditions
            price = planValidated.price
            duration = planValidated.duration
            visibility = planValidated.visibility
        }
    }
}