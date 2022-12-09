package subscription

import IInitObjects
import kotlinx.datetime.LocalDate
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import util.getNextMonthAsLocalDate
import util.getYesterdayAsLocalDate

abstract class BaseInitSubscriptions(val op: String): IInitObjects<Subscription> {
    open val lockOld: SubscriptionLock = SubscriptionLock("200_000_000")
    open val lockNew: SubscriptionLock = SubscriptionLock("200_000_001")
    open val lockBad: SubscriptionLock = SubscriptionLock("200_000_009")

    fun createInitTestModel(
        suf: String,
        ownerId: SbscrUserId = SbscrUserId("owner-123"),
        planId: PlanId = PlanId("plan-123"),
        startDate: LocalDate = getYesterdayAsLocalDate(),
        endDate: LocalDate = getNextMonthAsLocalDate(),
        paymentStatus: SbscrPaymentStatus = SbscrPaymentStatus.NOT_PAYED,
        isActive: Boolean = false,
        lock: SubscriptionLock = lockOld
    ) = Subscription(
           id = SubscriptionId(id = "subscription-repo-$op-$suf"),
           ownerId = ownerId,
           planId = planId,
           startDate = startDate,
           endDate = endDate,
           isActive = isActive,
           lock = lock,
           paymentStatus = paymentStatus
       )

}