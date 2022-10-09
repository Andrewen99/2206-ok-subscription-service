package stubs.subscription

import SubscriptionStubs
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.SbscrStubs

fun CorChainDsl<SubscriptionContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.SUCCESS && state == SbscrState.RUNNING }
    handle {
        state = SbscrState.FINISHING
        subscriptionResponses += SubscriptionStubs.SUBSCRIPTIONS
    }
}