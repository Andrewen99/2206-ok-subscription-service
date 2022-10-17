package stubs

import contexts.BaseContext
import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState

fun <T: BaseContext> CorChainDsl<T>.initStatus(title: String) = worker {
    this.title = title
    on { state == SbscrState.NONE}
    handle { state = SbscrState.RUNNING }
}