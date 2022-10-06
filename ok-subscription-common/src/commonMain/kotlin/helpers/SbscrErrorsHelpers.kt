package helpers

import contexts.BaseContext
import models.SbscrError
import models.SbscrState

fun Throwable.asSbscrError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = SbscrError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun BaseContext.fail(vararg error: SbscrError) {
    errors.addAll(error)
    state = SbscrState.FAILING
}