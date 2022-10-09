package stubs.subscription

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubValidationBadSearchParameters(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.BAD_SEARCH_PARAMETERS && state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                code = "validation-search-parameters",
                group = "validation",
                field = "boughtPeriod",
                message = "Bad search parameter 'boughtPeriod'",
            )
        )
    }
}