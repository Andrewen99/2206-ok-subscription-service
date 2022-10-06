package stubs

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on {stubCase == SbscrStubs.BAD_TITLE && state == SbscrState.RUNNING}
    handle {
        fail(
            SbscrError(
                code = "validation-title",
                group = "validation",
                field = "title",
                message = "Wrong title field"
            )
        )
    }

}