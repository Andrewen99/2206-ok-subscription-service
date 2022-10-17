package stubs.plan

import PlanStubs
import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs

fun CorChainDsl<PlanContext>.stubReadAllSuccess(title: String) = worker {
    this.title = title
    on {stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING}
    handle {
        state = SbscrState.FINISHING
        planResponses += PlanStubs.PLANS.toMutableList()
    }
}