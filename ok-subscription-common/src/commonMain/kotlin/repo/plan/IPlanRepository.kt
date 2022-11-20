package repo.plan

import models.SbscrError

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

        val resultErrorEmptyId = DbPlanResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank",
                )
            )
        )

        val resultErrorEmptyLock = DbPlanResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank",
                )
            )
        )

        fun resultErrorNotFound(key: String) = DbPlanResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "not-found",
                    field = "id",
                    group = "repo",
                    message = "Not Found object with id $key",
                )
            )
        )
    }
}