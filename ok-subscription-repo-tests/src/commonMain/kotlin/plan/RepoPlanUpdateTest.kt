package plan


import createConcurrencyAssertionErrorText
import createNotFoundAssertionErrorText
import models.SbscrError
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility
import repo.plan.DbPlanRequest
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class RepoPlanUpdateTest {
    abstract val repo: IPlanRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]

    private val reqUpdateSucc by lazy {
        Plan(
            id = updateSucc.id,
            title = "update-plan",
            conditions = mutableSetOf("update condition#1", "update condition#2"),
            duration = 5,
            price = "100",
            lock = initObjects[0].lock,
            visibility = SbscrPlanVisibility.PUBLIC
        )
    }

    private val reqUpdateNotFound by lazy {
        Plan(
            id = updateIdNotFound,
            title = "update obj not found",
            conditions = mutableSetOf("update obj not found cond"),
            duration = 5,
            price = "30000",
            visibility = SbscrPlanVisibility.PUBLIC,
            lock = initObjects[0].lock
        )
    }

    private val reqUpdateConc by lazy {
        Plan(
            id = updateConc.id,
            title = "update obj concurrency error",
            conditions = mutableSetOf("update obj concurrency error"),
            duration = 2,
            price = "999",
            lock = lockBad,
            visibility = SbscrPlanVisibility.PUBLIC
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePlan(DbPlanRequest(reqUpdateSucc))
        assertTrue(result.success)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.price, result.data?.price)
        assertEquals(reqUpdateSucc.duration, result.data?.duration)
        assertEquals(reqUpdateSucc.visibility, result.data?.visibility)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePlan(DbPlanRequest(reqUpdateNotFound))
        assertFalse(result.success)
        assertEquals(null, result.data)
        assertTrue(
            result.errors.any { it.field == "id" && it.group == "repo" && it.code == "not-found" },
            createNotFoundAssertionErrorText(result.errors)
        )
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updatePlan(DbPlanRequest(reqUpdateConc))
        assertFalse(result.success)
        assertTrue(
            result.errors.any { it.field == "lock" && it.group == "repo" && it.code == "concurrency" },
            createConcurrencyAssertionErrorText(result.errors)
        )
    }

    companion object : BaseInitPlans("update") {
        override val initObjects:List<Plan> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc")
        )

        val updateIdNotFound = PlanId("plan-repo-update-not-found")

    }
}