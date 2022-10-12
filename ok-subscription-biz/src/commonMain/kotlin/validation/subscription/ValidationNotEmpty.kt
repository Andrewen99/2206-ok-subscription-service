package validation.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import helpers.errorValidation
import helpers.fail

fun CorChainDsl<SubscriptionContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on {
        subscriptionValidating.id.asString().isEmpty()
    }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<SubscriptionContext>.validatePlanIdNotEmpty(title: String) = worker {
    this.title = title
    on {
        subscriptionValidating.planId.asString().isEmpty()
    }
    handle {
        fail(
            errorValidation(
                field = "planId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
