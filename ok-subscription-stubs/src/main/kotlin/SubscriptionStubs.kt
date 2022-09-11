import PlanStubs.PLAN1
import PlanStubs.PLAN2
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import java.time.LocalDate

object SubscriptionStubs {
    fun get(): Subscription = SUBSCRIPTION1.copy()
    fun getAll(): List<Subscription> = listOf(SUBSCRIPTION1.copy(), SUBSCRIPTION2.copy())

    private val startDate = LocalDate.now().minusDays(1)
    private val SUBSCRIPTION1 = Subscription(
        id = SubscriptionId(id = "sub-id-11"),
        planId = PLAN1.id,
        startDate = startDate,
        endDate = startDate.plusMonths(3),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.PAYED
    )
    private val SUBSCRIPTION2 = Subscription(
        id = SubscriptionId(id = "sub-id-22"),
        planId = PLAN2.id,
        startDate = startDate,
        endDate = startDate.plusMonths(6),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.NOT_PAYED
    )
}