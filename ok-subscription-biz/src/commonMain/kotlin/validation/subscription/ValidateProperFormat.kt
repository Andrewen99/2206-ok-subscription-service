package validation.subscription

import ID_FORMAT_REGEX
import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import helpers.errorValidation
import helpers.fail
import models.plan.PlanId
import models.subscription.SubscriptionId
import validation.handleBadFormat

fun CorChainDsl<SubscriptionContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    on {subscriptionValidating.id != SubscriptionId.NONE && !subscriptionValidating.id.asString().matches(ID_FORMAT_REGEX)}
    handle {
        handleBadFormat(subscriptionValidating.id.asString(), "id")
    }
}

fun CorChainDsl<SubscriptionContext>.validatePlanIdProperFormat(title: String) = worker {
    this.title = title
    on {subscriptionValidating.planId != PlanId.NONE && !subscriptionValidating.planId.asString().matches(ID_FORMAT_REGEX)}
    handle {
        handleBadFormat(subscriptionValidating.planId.asString(), "planId")
    }
}