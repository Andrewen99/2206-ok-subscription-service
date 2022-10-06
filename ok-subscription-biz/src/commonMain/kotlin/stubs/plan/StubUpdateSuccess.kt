package stubs.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs

fun CorChainDsl<PlanContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on {stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING}
    handle {
        state = SbscrState.FINISHING
        val stub = PlanStubs.PLAN1.apply {
            planRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            planRequest.conditions.takeIf { it.isNotEmpty() }?.also {
                this.conditions.clear()
                this.conditions += it
            }
            planRequest.duration.takeIf { it != 0 }?.also {this.duration = it }
            planRequest.price.takeIf { it.isNotBlank() }?.also { this.price = it }
        }
        planResponse = stub
    }
}