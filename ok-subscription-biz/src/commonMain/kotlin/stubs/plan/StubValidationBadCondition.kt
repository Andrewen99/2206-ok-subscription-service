package stubs.plan

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubValidationBadCondition(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.BAD_CONDITION && state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                code = "validation-condition",
                group = "validation",
                field = "condition",
                message = "Wrong condition description",
            )
        )
    }
}