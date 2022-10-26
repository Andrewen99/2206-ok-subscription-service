package repo.plan

interface IPlanRepository {

    suspend fun createPlan(rq: DbPlanRequest) : DbPlanResponse

    suspend fun readPlan(rq: DbPlanIdRequest) : DbPlanResponse

    suspend fun updatePlan(rq: DbPlanRequest) : DbPlanResponse

    suspend fun deletePlan(rq: DbPlanIdRequest) : DbPlanResponse

    suspend fun readAllPlans() : DbPlansResponse

    companion object {
        val NONE = object : IPlanRepository {
            override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updatePlan(rq: DbPlanRequest): DbPlanResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deletePlan(rq: DbPlanIdRequest): DbPlanResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readAllPlans(): DbPlansResponse {
                TODO("Not yet implemented")
            }

        }
    }
}