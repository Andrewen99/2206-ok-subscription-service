package stubs

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubDbError(title: String) = worker {
    this.title = title
    on {stubCase == SbscrStubs.DB_ERROR && state == SbscrState.RUNNING}
    handle {
        fail(
            SbscrError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}