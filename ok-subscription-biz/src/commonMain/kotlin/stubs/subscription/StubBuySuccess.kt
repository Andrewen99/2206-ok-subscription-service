package stubs.subscription

import SubscriptionStubs
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs
import models.plan.PlanId

fun CorChainDsl<SubscriptionContext>.stubBuySuccess(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING }
    handle {
        state = SbscrState.FINISHING
        val stub = SubscriptionStubs.SUBSCRIPTION2.apply {
            subscriptionRequest.planId.takeIf { it != PlanId.NONE }?.also { this.planId = it }
        }
        subscriptionResponse = stub
    }
}