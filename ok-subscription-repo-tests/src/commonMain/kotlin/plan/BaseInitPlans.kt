package plan

import IInitObjects
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility
import models.subscription.SubscriptionId

/**
 * Абстрактный класс, используемый для заполнения репозитария значениями
 */
abstract class BaseInitPlans(val op: String): IInitObjects<Plan> {

    open val lockOld: PlanLock = PlanLock("200_000_000")
    open val lockNew: PlanLock = PlanLock("200_000_001")
    open val lockBad: PlanLock = PlanLock("200_000_009")

    fun createInitTestModel(
        suf: String,
        lock: PlanLock = lockOld
    ) = Plan(
        id = PlanId("plan-repo-$op-$suf"),
        title = "$suf stub",
        conditions = mutableSetOf("stub condition#1", "stub condition#2"),
        duration = 0,
        price = "99999999",
        lock = lock,
        visibility = SbscrPlanVisibility.PUBLIC
    )
}