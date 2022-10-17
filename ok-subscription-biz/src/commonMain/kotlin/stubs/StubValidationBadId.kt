package stubs

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.BAD_ID && state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}