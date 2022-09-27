class CorChain<T>(
    title: String,
    description: String = "",
    val execs: List<ICorExec<T>>,
    blockOn: T.() -> Boolean = {true},
    blockExcept: T.(ex: Throwable) -> Unit = {ex -> ex.printStackTrace()},
    val handler: suspend (T, List<ICorExec<T>>) -> Unit
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(ctx: T) = handler(ctx, execs)
}

