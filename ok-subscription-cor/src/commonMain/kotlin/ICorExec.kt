interface ICorExec<T> {

    val title: String
    val description: String
    suspend fun exec(ctx: T)
}