import PlanStubs.PLAN1
import PlanStubs.PLAN2
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import java.time.LocalDate

object SubscriptionStubs {

    private val startDate = LocalDate.now().minusDays(1)
    val SUBSCRIPTION1
    get() = Subscription(
        id = SubscriptionId(id = "sub-id-11"),
        planId = PLAN1.id,
        startDate = startDate,
        endDate = startDate.plusMonths(3),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.PAYED
    )
    val SUBSCRIPTION2
        get() = Subscription(
        id = SubscriptionId(id = "sub-id-22"),
        planId = PLAN2.id,
        startDate = startDate,
        endDate = startDate.plusMonths(6),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.NOT_PAYED
    )

    val SUBSCRIPTIONS
        get() = listOf(SUBSCRIPTION1, SUBSCRIPTION2)


}