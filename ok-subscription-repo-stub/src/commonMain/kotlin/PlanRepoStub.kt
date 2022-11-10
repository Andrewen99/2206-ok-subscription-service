import models.plan.Plan
import repo.plan.*

class PlanRepoStub : IPlanRepository {
    override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1,
            success = true
        )
    }

    override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
        return DbPlanResponse(
            data = PlanStubs.PLAN1,
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

    override suspend fun readAllPlans(): DbPlansResponse {
        return DbPlansResponse(
            data = PlanStubs.PLANS,
            success = true
        )
    }
}