package validation

import contexts.BaseContext
import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import models.subscription.SubscriptionCommand

fun CorChainDsl<PlanContext>.finishPlanValidation(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        planValidated = planValidating
    }
}

fun CorChainDsl<SubscriptionContext>.finishSubscriptionValidation(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        subscriptionValidated = subscriptionValidating
    }
}

fun CorChainDsl<SubscriptionContext>.finishSubscriptionFilterValidation(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        subscriptionFilterValidated = subscriptionFilterValidating
    }
}

