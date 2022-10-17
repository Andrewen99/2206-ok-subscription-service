package validation.plan

import ID_FORMAT_REGEX
import PRICE_FORMAT_REGEX
import HAS_CONTENT_REGEX
import contexts.PlanContext
import dsl.CorChainDsl
import dsl.worker
import helpers.errorValidation
import helpers.fail
import models.plan.PlanId
import validation.handleBadFormat

fun CorChainDsl<PlanContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    on {planValidating.id != PlanId.NONE && !planValidating.id.asString().matches(ID_FORMAT_REGEX)}
    handle {
        handleBadFormat(planValidating.id.asString(), "id")
    }
}

fun CorChainDsl<PlanContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    on {planValidating.title.isNotEmpty() && !planValidating.title.contains(HAS_CONTENT_REGEX)}
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "badFormat",
                description = "field must contain letters"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validateDurationIsPositive(title: String) = worker {
    this.title = title
    on {planValidating.duration <= 0}
    handle {
        fail(
            errorValidation(
                field = "duration",
                violationCode = "badFormat",
                description = "must be greater than 0"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validatePriceProperFormat(title: String) = worker {
    this.title = title
    on {planValidating.price.isNotEmpty() && !planValidating.price.matches(PRICE_FORMAT_REGEX)}
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "badFormat",
                description = "should match regex ${PRICE_FORMAT_REGEX.pattern}"
            )
        )
    }
}

fun CorChainDsl<PlanContext>.validateConditions(title: String) = worker {
    this.title = title
    on {planValidating.conditions.isNotEmpty() && planValidating.conditions.any { !it.contains(HAS_CONTENT_REGEX)}}
    handle {
        fail(
            errorValidation(
                field = "conditions",
                violationCode = "badFormat",
                description = "field must contain letters"
            )
        )
    }
}








