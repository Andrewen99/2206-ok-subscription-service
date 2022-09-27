class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = {true},
    private val blockHandle: suspend T.() -> Unit,
    blockExcept: suspend T.(ex: Throwable) -> Unit = {}
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(ctx: T) = ctx.blockHandle()

}