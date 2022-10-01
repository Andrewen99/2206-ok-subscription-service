import contexts.PlanContext
import dsl.rootChain

class PlanProcessor {
    suspend fun exec(ctx: PlanContext) = PlanChain.exec(ctx)

    companion object {
        private val PlanChain = rootChain<PlanContext> {

        }.build()
    }
}