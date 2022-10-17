package dsl

import CorWorker
import ICorExec

@CorDslMarker
interface ICorExecDsl<T> {
    fun build(): ICorExec<T>
}

@CorDslMarker
abstract class CorExecDsl<T>(
    var title: String,
    var description: String,
    var blockOn: suspend T.() -> Boolean,
    var blockExcept: suspend T.(ex: Throwable) -> Unit
) : ICorExecDsl<T> {
    fun on(onDsl: suspend T.() -> Boolean) {
        blockOn = onDsl
    }

    fun except(exceptDsl: suspend T.(ex: Throwable) -> Unit) {
        blockExcept = exceptDsl
    }
}
