package stubs

import PlanProcessor
import models.SbscrRequestId
import models.plan.Plan
import models.plan.PlanId
import models.plan.SbscrPlanVisibility

object TestConstants {
    val PROCESSOR: PlanProcessor
        get() = PlanProcessor()
    val REQUEST_ID = SbscrRequestId("req111")
    val ID = PlanId("111")
    val TITLE = "title 111"
    val CONDITIONS: MutableSet<String> = mutableSetOf("condition #1", "condition #2")
    var DURATION: Int = 6
    var PRICE: String = "1000"
    var VISIBILITY: SbscrPlanVisibility = SbscrPlanVisibility.PUBLIC
    val PLAN_REQUEST: Plan
        get() = Plan(
        id = ID,
        title = TITLE,
        conditions = CONDITIONS,
        duration = DURATION,
        price = PRICE,
        visibility = VISIBILITY
    )
}