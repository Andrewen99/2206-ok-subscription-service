package stubs.subscription

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import models.SbscrStubs

fun <T: BaseContext> CorChainDsl<T>.stubCannotBuy(title: String) = worker {
    this.title = title
    on { stubCase == SbscrStubs.CANNOT_BUY && state == SbscrState.RUNNING }
    handle {
        fail(
            SbscrError(
                code = "error-cannot-buy",
                group = "error",
                message = "Error. Not able to acquire/buy this plan",
            )
        )
    }
}