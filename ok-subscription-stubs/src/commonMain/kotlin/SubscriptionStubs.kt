import PlanStubs.PLAN1
import PlanStubs.PLAN2
import kotlinx.datetime.*
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId

object SubscriptionStubs {

    private val startDate =
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
            .minus(1, DateTimeUnit.DAY)
    val SUBSCRIPTION1
    get() = Subscription(
        id = SubscriptionId(id = "sub-id-11"),
        planId = PLAN1.id,
        startDate = startDate,
        endDate = startDate.plus(3, DateTimeUnit.MONTH),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.PAYED
    )
    val SUBSCRIPTION2
        get() = Subscription(
        id = SubscriptionId(id = "sub-id-22"),
        planId = PLAN2.id,
        startDate = startDate,
        endDate = startDate.plus(6, DateTimeUnit.MONTH),
        isActive = true,
        paymentStatus = SbscrPaymentStatus.NOT_PAYED
    )

    val SUBSCRIPTIONS
        get() = listOf(SUBSCRIPTION1, SUBSCRIPTION2)


}