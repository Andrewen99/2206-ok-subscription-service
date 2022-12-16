package repo.plan

import models.plan.PlanFilter
import models.plan.SbscrPlanVisibility

data class DbPlanFilterRequest(
    val filter: PlanFilter
) {
    constructor(visibilitySet: Set<SbscrPlanVisibility>) : this(PlanFilter(visibilitySet))
}
