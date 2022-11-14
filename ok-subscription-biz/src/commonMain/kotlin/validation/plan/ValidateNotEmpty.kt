package validation.plan

import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import helpers.errorValidation
import helpers.fail

fun CorChainDsl<PlanContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { planValidating.id.asString().isEmpty() }
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

fun CorChainDsl<PlanContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { planValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { planValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validateConditionsNotEmpty(title: String) = worker {
    this.title = title
    on { planValidating.conditions.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "conditions",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validatePriceNotEmpty(title: String)  = worker {
    this.title = title
    on{ planValidating.price.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
