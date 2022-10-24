package stubs

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.chain
import models.SbscrState
import models.SbscrWorkMode

fun <T : BaseContext> CorChainDsl<T>.stubs(title: String, block: CorChainDsl<T>.() -> Unit) = chain {
    this.title = title
    on { workMode == SbscrWorkMode.STUB && state == SbscrState.RUNNING }
    block()
}