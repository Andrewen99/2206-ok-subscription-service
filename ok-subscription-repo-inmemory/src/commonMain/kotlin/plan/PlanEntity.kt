package plan

import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility

data class PlanEntity(
    val id: String? = null,
    val title: String? = null,
    val conditions: String? = null,
    val duration: Int? = null,
    val price: String? = null,
    val lock: String? = null,
    val visibility: String? = null
) {
    constructor(model: Plan) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        conditions = model.conditions.takeIf{it.isNotEmpty()}?.joinToString("~~"),
        duration = model.duration.takeIf { it != 0 },
        price = model.price.takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != SbscrPlanVisibility.NONE }?.name
    )

    fun toInternal() = Plan(
        id = id?.let { PlanId(id) } ?: PlanId.NONE,
        title = title ?: "",
        conditions = conditions?.let { it.split("~~").toMutableSet() } ?: mutableSetOf(),
        duration = duration ?: 0,
        price = price ?: "0",
        lock = lock?.let { PlanLock(it) } ?: PlanLock.NONE,
        visibility = visibility?.let { SbscrPlanVisibility.valueOf(it) } ?: SbscrPlanVisibility.NONE
    )
}