package general

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.chain
import models.plan.PlanCommand
import models.subscription.SubscriptionCommand

fun CorChainDsl<PlanContext>.operation(title: String, planCommand: PlanCommand, block: CorChainDsl<PlanContext>.() -> Unit) = chain {
    block()
    this.title = title
    on {command == planCommand}
}

fun CorChainDsl<SubscriptionContext>.operation(title: String, subCommand: SubscriptionCommand, block: CorChainDsl<SubscriptionContext>.() -> Unit) = chain {
    block()
    this.title = title
    on {command == subCommand}
}