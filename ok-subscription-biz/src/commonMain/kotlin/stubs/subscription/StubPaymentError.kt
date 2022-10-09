package stubs.subscription

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubPaymentError(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.PAYMENT_ERROR && state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                code = "error-unsuccessful-payment",
                group = "error",
                message = "Error. Unsuccessful payment",
            )
        )
    }
}