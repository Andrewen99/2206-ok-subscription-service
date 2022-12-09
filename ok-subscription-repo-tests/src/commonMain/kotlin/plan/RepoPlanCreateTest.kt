package plan

import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility
import repo.plan.DbPlanRequest
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoPlanCreateTest {
    abstract val repo: IPlanRepository

    protected open val createObj = Plan(
        title = "create object",
        conditions = mutableSetOf("create condition#1", "create condition#2"),
        duration = 5,
        price = "9999999",
        visibility = SbscrPlanVisibility.PUBLIC
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPlan(DbPlanRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: PlanId.NONE)
        assertTrue(result.success)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.conditions, result.data?.conditions)
        assertEquals(expected.duration, result.data?.duration)
        assertEquals(expected.price, result.data?.price)
        assertEquals(expected.visibility, result.data?.visibility)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitPlans("create") {
        override val initObjects: List<Plan> = emptyList()
    }
}