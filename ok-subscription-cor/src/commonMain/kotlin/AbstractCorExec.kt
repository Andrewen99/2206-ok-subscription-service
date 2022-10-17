abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    val blockOn: suspend T.() -> Boolean = { true },
    val blockExcept: suspend T.(ex: Throwable) -> Unit = {}
) : ICorExec<T> {
    protected abstract suspend fun handle(ctx: T)

    private suspend fun on(ctx: T) = ctx.blockOn()

    private suspend fun except(ctx: T, ex: Throwable) = ctx.blockExcept(ex)

    override suspend fun exec(ctx: T) {
        if (on(ctx)) {
            try {
                handle(ctx)
            } catch (ex: Throwable) {
                except(ctx, ex)
            }
        }
    }
}