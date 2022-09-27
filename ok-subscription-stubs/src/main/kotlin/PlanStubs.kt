import models.plan.Plan
import models.plan.PlanId
import models.plan.SbscrPlanVisibility

object PlanStubs {

    val PLAN1
        get() = Plan(
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

    val PLAN2
        get() = Plan(
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

    val PLAN3
        get() = Plan(
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

    val PLANS
        get() = listOf(PLAN1, PLAN2, PLAN3)
}