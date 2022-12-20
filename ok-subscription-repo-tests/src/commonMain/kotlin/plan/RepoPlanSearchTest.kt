package plan

import models.plan.Plan
import models.plan.SbscrPlanVisibility
import repo.plan.DbPlanFilterRequest
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class RepoPlanSearchTest {
    abstract val repo: IPlanRepository

    @Test
    fun searchPublic() = runRepoTest {
        val result = repo.searchPlans(DbPlanFilterRequest(setOf(SbscrPlanVisibility.PUBLIC)))
        assertTrue(result.success)
        assertEquals(initObjects.subList(0,2), result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchAdminOnly() = runRepoTest {
        val result = repo.searchPlans(DbPlanFilterRequest(setOf(SbscrPlanVisibility.ADMIN_ONLY)))
        assertTrue(result.success)
        assertEquals(initObjects.subList(2,3), result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchAll() = runRepoTest {
        val result = repo.searchPlans(DbPlanFilterRequest(setOf(SbscrPlanVisibility.PUBLIC, SbscrPlanVisibility.ADMIN_ONLY)))
        assertTrue(result.success)
        assertEquals(initObjects, result.data)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitPlans("readAll") {
        override val initObjects: List<Plan> = listOf(
            createInitTestModel(suf = "readAll-obj1", visibility = SbscrPlanVisibility.PUBLIC),
            createInitTestModel(suf = "readAll-obj2", visibility = SbscrPlanVisibility.PUBLIC),
            createInitTestModel(suf = "readAll-obj3", visibility = SbscrPlanVisibility.ADMIN_ONLY)
        )
    }
}