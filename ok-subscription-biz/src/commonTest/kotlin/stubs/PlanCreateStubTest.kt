package stubs

import PlanProcessor
import PlanStubs
import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrRequestId
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.plan.Plan
import models.plan.PlanCommand
import models.plan.PlanId
import models.plan.SbscrPlanVisibility
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanCreateStubTest {

    private val processor = PlanProcessor()
    val requestId = SbscrRequestId("req111")
    val id = PlanId("111")
    val title = "title 111"
    val conditions: MutableSet<String> = mutableSetOf("condition #1", "condition #2")
    var duration: Int = 6
    var price: String = "1000"
    var visibility: SbscrPlanVisibility = SbscrPlanVisibility.PUBLIC
    val planRequest = Plan(
            id = id,
            title = title,
            conditions = conditions,
            duration = duration,
            price = price,
            visibility = visibility
        )

    @Test
    fun create() = runTest {
        val ctx = PlanContext(
            command = PlanCommand.CREATE,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = requestId,
            planRequest = planRequest
        )
        processor.exec(ctx)
        assertEquals(PlanStubs.PLAN1.id, ctx.planResponse.id)
        assertEquals(title, ctx.planResponse.title)
        assertEquals(conditions, ctx.planResponse.conditions)
        assertEquals(duration, ctx.planResponse.duration)
        assertEquals(price, ctx.planResponse.price)
        assertEquals(visibility, ctx.planResponse.visibility)
    }

}