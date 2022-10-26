package repo.plan

import models.SbscrError
import models.plan.Plan
import repo.IDbResponse

data class DbPlansResponse(
    override val data: List<Plan>?,
    override val success: Boolean,
    override val errors: List<SbscrError>
) : IDbResponse<List<Plan>>