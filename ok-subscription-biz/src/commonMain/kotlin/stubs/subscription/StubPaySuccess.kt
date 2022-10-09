package stubs.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs

fun CorChainDsl<SubscriptionContext>.stubPaySuccess(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING }
    handle {
        state = SbscrState.FINISHING
        val stub = SubscriptionStubs.SUBSCRIPTION1.apply {
            planId = subscriptionRequest.planId
        }
        subscriptionResponse = stub
    }
}