package stubs

import PlanStubs
import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.plan.Plan
import models.plan.PlanCommand
import models.plan.PlanId
import stubs.TestConstants.CONDITIONS
import stubs.TestConstants.DURATION
import stubs.TestConstants.PLAN_REQUEST
import stubs.TestConstants.PRICE
import stubs.TestConstants.PROCESSOR
import stubs.TestConstants.REQUEST_ID
import stubs.TestConstants.TITLE
import stubs.TestConstants.VISIBILITY
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanSuccessStubTest {

    @Test
    fun create() = runTest {
        val ctx = PlanContext(
            command = PlanCommand.CREATE,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            planRequest = PLAN_REQUEST
        )
        PROCESSOR.exec(ctx)
        assertEquals(PlanStubs.PLAN1.id, ctx.planResponse.id)
        assertEquals(TITLE, ctx.planResponse.title)
        assertEquals(CONDITIONS, ctx.planResponse.conditions)
        assertEquals(DURATION, ctx.planResponse.duration)
        assertEquals(PRICE, ctx.planResponse.price)
        assertEquals(VISIBILITY, ctx.planResponse.visibility)
    }

    @Test
    fun update() = runTest {
        val ctx = PlanContext(
            command = PlanCommand.UPDATE,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            planRequest = PLAN_REQUEST
        )
        PROCESSOR.exec(ctx)
        assertEquals(PlanStubs.PLAN1.id, ctx.planResponse.id)
        assertEquals(TITLE, ctx.planResponse.title)
        assertEquals(CONDITIONS, ctx.planResponse.conditions)
        assertEquals(DURATION, ctx.planResponse.duration)
        assertEquals(PRICE, ctx.planResponse.price)
        assertEquals(VISIBILITY, ctx.planResponse.visibility)
    }

    @Test
    fun read() = runTest {
        val planId = PlanId("pl-id-1")
        val ctx = PlanContext(
            command = PlanCommand.READ,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            planRequest = Plan(
                id = planId
            )
        )
        PROCESSOR.exec(ctx)
        val expectedPlan = PlanStubs.PLAN1
        assertEquals(planId, ctx.planResponse.id)
        assertEquals(expectedPlan.title, ctx.planResponse.title)
        assertEquals(expectedPlan.conditions, ctx.planResponse.conditions)
        assertEquals(expectedPlan.duration, ctx.planResponse.duration)
        assertEquals(expectedPlan.price, ctx.planResponse.price)
        assertEquals(expectedPlan.visibility, ctx.planResponse.visibility)
    }

    @Test
    fun readAll() = runTest {
        val ctx = PlanContext(
            command = PlanCommand.READ_ALL,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
        )
        PROCESSOR.exec(ctx)
        val expectedPlan = PlanStubs.PLAN1
        assertEquals(PlanStubs.PLANS.size, ctx.planResponses.size)
        assertEquals(expectedPlan.title, ctx.planResponses[0].title)
        assertEquals(expectedPlan.conditions, ctx.planResponses[0].conditions)
        assertEquals(expectedPlan.duration, ctx.planResponses[0].duration)
        assertEquals(expectedPlan.price, ctx.planResponses[0].price)
        assertEquals(expectedPlan.visibility, ctx.planResponses[0].visibility)
    }

    @Test
    fun delete() = runTest {
        val planId = PlanId("pl-id-1")
        val ctx = PlanContext(
            command = PlanCommand.DELETE,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            planRequest = Plan(
                id = planId
            )
        )
        PROCESSOR.exec(ctx)
        val expectedPlan = PlanStubs.PLAN1
        assertEquals(planId, ctx.planResponse.id)
        assertEquals(expectedPlan.title, ctx.planResponse.title)
        assertEquals(expectedPlan.conditions, ctx.planResponse.conditions)
        assertEquals(expectedPlan.duration, ctx.planResponse.duration)
        assertEquals(expectedPlan.price, ctx.planResponse.price)
        assertEquals(expectedPlan.visibility, ctx.planResponse.visibility)
    }

}