package stubs

import PlanProcessor
import SubscriptionProcessor
import models.SbscrRequestId
import models.plan.Plan
import models.plan.PlanId
import models.plan.SbscrPlanVisibility
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionFilter
import models.subscription.SubscriptionId

object TestConstants {
    val PLAN_PROCESSOR: PlanProcessor
        get() = PlanProcessor()
    val REQUEST_ID = SbscrRequestId("req111")
    val PLAN_ID = PlanId("111")
    val PLAN_TITLE = "title 111"
    val PLAN_CONDITIONS: MutableSet<String> = mutableSetOf("condition #1", "condition #2")
    var PLAN_DURATION: Int = 6
    var PLAN_PRICE: String = "1000"
    var PLAN_VISIBILITY: SbscrPlanVisibility = SbscrPlanVisibility.PUBLIC
    val PLAN_REQUEST: Plan
        get() = Plan(
        id = PLAN_ID,
        title = PLAN_TITLE,
        conditions = PLAN_CONDITIONS,
        duration = PLAN_DURATION,
        price = PLAN_PRICE,
        visibility = PLAN_VISIBILITY
    )

    val SUB_PROCESSOR: SubscriptionProcessor
        get() = SubscriptionProcessor()
    val SUB_ID = SubscriptionId("sub111")
    val READ_PAY_SUBSCRIPTION_REQUEST: Subscription
        get() = Subscription(
            id = SUB_ID
        )
    val BUY_SUBSCRIPTION_REQUEST: Subscription
        get() = Subscription(
            planId = PLAN_ID
        )

    val SEARCH_SUBSCRIPTION_REQUEST: SubscriptionFilter
        get() = SubscriptionFilter(
            planId = PLAN_ID
        )
}