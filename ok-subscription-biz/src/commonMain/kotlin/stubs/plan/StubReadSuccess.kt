package stubs.plan

import PlanStubs
import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs
import models.plan.PlanId

fun CorChainDsl<PlanContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING  }
    handle {
        state = SbscrState.FINISHING
        val stub = PlanStubs.PLAN1.apply {
            planRequest.id.takeIf { it != PlanId.NONE }?.also { this.id = it }
        }
        planResponse = stub
    }
}