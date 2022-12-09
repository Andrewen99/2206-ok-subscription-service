package repo.plan

import models.SbscrError
import models.plan.Plan
import repo.IDbResponse

data class DbPlanResponse(
    override val data: Plan?,
    override val success: Boolean,
    override val errors: List<SbscrError> = emptyList()
) : IDbResponse<Plan>
