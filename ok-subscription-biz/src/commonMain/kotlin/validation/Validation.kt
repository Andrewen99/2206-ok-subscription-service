package validation

import contexts.BaseContext
import dsl.CorChainDsl
import dsl.chain
import models.SbscrState

fun <T: BaseContext> CorChainDsl<T>.validation(block: CorChainDsl<T>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == SbscrState.RUNNING }
}