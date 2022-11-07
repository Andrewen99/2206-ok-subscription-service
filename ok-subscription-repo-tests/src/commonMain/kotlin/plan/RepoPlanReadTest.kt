package plan

import kotlinx.coroutines.runBlocking
import models.SbscrError
import models.plan.Plan
import models.plan.PlanId
import repo.plan.DbPlanIdRequest
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.*

abstract class RepoPlanReadTest {
    abstract val repo: IPlanRepository
    protected open val readSucc = Companion.initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPlan(DbPlanIdRequest(readSucc.id))

        assertTrue(result.success)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readPlan(DbPlanIdRequest(notFoundId))

        assertFalse(result.success)
        assertEquals(null, result.data)
        assertEquals(
            listOf(SbscrError(field = "id", code = "Not Found")),
            result.errors
        )
    }


    companion object: BaseInitPlans("read") {
        override val initObjects: List<Plan> = listOf(
            createInitTestModel("read")
        )
        private val notFoundId = PlanId("plan-repo-read-not-found")
    }
}