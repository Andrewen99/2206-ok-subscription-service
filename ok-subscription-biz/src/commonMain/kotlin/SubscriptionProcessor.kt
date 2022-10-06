import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.rootChain

class SubscriptionProcessor {
    suspend fun exec(ctx: SubscriptionContext) = SubscriptionChain.exec(ctx)

    companion object {
        private val SubscriptionChain = rootChain<SubscriptionContext> {

        }.build()
    }
}