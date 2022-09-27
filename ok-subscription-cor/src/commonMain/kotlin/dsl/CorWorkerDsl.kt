package dsl

import CorWorker
import ICorExec

@CorDslMarker
class CorWorkerDsl<T>(
    title: String = "",
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private var blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(ex: Throwable) -> Unit = {}
) : CorExecDsl<T>(title, description, blockOn, blockExcept) {
    override fun build(): ICorExec<T> {
        return CorWorker(
            title = title,
            description = description,
            blockOn = blockOn,
            blockHandle = blockHandle,
            blockExcept = blockExcept
        )
    }

    fun handle(handler: suspend T.() -> Unit) {
        blockHandle = handler
    }
}