package general.plan

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrWorkMode

fun CorChainDsl<PlanContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != SbscrWorkMode.STUB }
    handle {
        planResponse = planRepoDone
        planResponses = plansRepoDone
        state = when(val st = state) {
            SbscrState.RUNNING -> SbscrState.FINISHING
            else -> st
        }
    }
}

