package repo.plan

import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock

data class DbPlanIdRequest(
    val id: PlanId,
    val lock: PlanLock = PlanLock.NONE
) {
    constructor(plan: Plan): this (plan.id, plan.lock)
}