package plan

import models.plan.Plan
import repo.plan.IPlanRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class RepoPlanReadAllTest {
    abstract val repo: IPlanRepository
    protected open val readAllSucc = initObjects

    @Test
    fun readAll() = runRepoTest {
        val result = repo.readAllPlans()
        assertTrue(result.success)
        assertEquals(initObjects, result.data)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitPlans("readAll") {
        override val initObjects: List<Plan> = listOf(
            createInitTestModel("readAll-obj1"),
            createInitTestModel("readAll-obj2"),
            createInitTestModel("readAll-obj3")
        )
    }
}