import models.plan.Plan
import models.plan.PlanId
import models.plan.SbscrPlanVisibility

object PlanStubs {
    fun get(): Plan = PLAN1.copy()
    fun getAll(): List<Plan> = listOf(PLAN1.copy(), PLAN2.copy(), PLAN3.copy())

    internal val PLAN1 = Plan(
        id = PlanId("pl-id-10"),
        title = "3 месячный план",
        conditions = mutableSetOf(
            "3 бесплатные доставки в месяц",
            "Скидка 5% на все товары",
            "Персональные скидки"
        ),
        duration = 3,
        price = "1200",
        visibility = SbscrPlanVisibility.PUBLIC
    )

    internal val PLAN2 = Plan(
        id = PlanId("pl-id-20"),
        title = "6 месячный план",
        conditions = mutableSetOf(
            "4 бесплатные доставки в месяц",
            "Скидка 7% на все товары",
            "Персональные скидки"
        ),
        duration = 3,
        price = "2400",
        visibility = SbscrPlanVisibility.PUBLIC
    )

    private val PLAN3 = Plan(
        id = PlanId("pl-id-20"),
        title = "6 месячный план",
        conditions = mutableSetOf(
            "4 бесплатные доставки в месяц",
            "Скидка 7% на все товары",
            "Персональные скидки"
        ),
        duration = 3,
        price = "2400",
        visibility = SbscrPlanVisibility.PUBLIC
    )
}