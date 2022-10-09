package stubs.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs
import models.subscription.SubscriptionId

fun CorChainDsl<SubscriptionContext>.stubPaySuccess(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING }
    handle {
        state = SbscrState.FINISHING
        val stub = SubscriptionStubs.SUBSCRIPTION1.apply {
            subscriptionRequest.id.takeIf { it != SubscriptionId.NONE }?.also { this.id = it}
        }
        subscriptionResponse = stub
    }
}