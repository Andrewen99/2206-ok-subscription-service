import models.plan.SbscrPlanVisibility
import repo.plan.*

class PlanRepoStub(val visibility: SbscrPlanVisibility = SbscrPlanVisibility.PUBLIC) : IPlanRepository {
    override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1,
            success = true
        )
    }

    override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1.also { it.visibility = visibility },
            success = true
        )
    }

    override suspend fun updatePlan(rq: DbPlanRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1,
            success = true
        )
    }

    override suspend fun deletePlan(rq: DbPlanIdRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1,
            success = true
        )
    }

    override suspend fun searchPlans(rq: DbPlanFilterRequest): DbPlansResponse {
        return DbPlansResponse(
            data = PlanStubs.PLANS,
            success = true
        )
    }
}