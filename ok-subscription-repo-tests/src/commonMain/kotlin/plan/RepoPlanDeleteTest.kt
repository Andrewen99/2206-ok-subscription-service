package plan

import models.SbscrError
import models.plan.Plan
import models.plan.PlanId
import repo.plan.DbPlanIdRequest
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class RepoPlanDeleteTest {
    abstract val repo: IPlanRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deletePlan(DbPlanIdRequest(deleteSucc))

        assertTrue(result.success)
        assertEquals(emptyList(), result.errors)
        assertEquals(deleteSucc.lock, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deletePlan(DbPlanIdRequest(notFoundId))

        assertFalse(result.success)
        assertEquals(
            listOf(SbscrError(code = "notFound", field = "id")),
            result.errors
        )
    }

    @Test
    fun deleteConcurrencyError() = runRepoTest {
        val result = repo.deletePlan(DbPlanIdRequest(deleteConc.id, lockBad))

        assertFalse(result.success)
        assertEquals(
            listOf(SbscrError(code = "concurrency", field = "lock")),
            result.errors
        )
    }

    companion object: BaseInitPlans("delete") {
        override val initObjects: List<Plan> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("delete-lock")
        )
        val notFoundId = PlanId("plan-repo-delete-notFound")
    }
}