package stubs

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState

fun <T: BaseContext> CorChainDsl<T>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}