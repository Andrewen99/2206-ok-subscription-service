package stubs

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun CorChainDsl<PlanContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == SbscrState.NONE}
    handle { state == SbscrState.RUNNING }
}