package general

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.chain
import dsl.worker
import models.plan.Plan
import models.plan.PlanCommand

fun CorChainDsl<PlanContext>.operation(title: String, planCommand: PlanCommand, block: CorChainDsl<PlanContext>.() -> Unit) = chain {
    block()
    this.title = title
    on {command == planCommand}
}