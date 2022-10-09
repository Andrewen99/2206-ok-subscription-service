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
import stubs.TestConstants.PLAN_CONDITIONS
import stubs.TestConstants.PLAN_DURATION
import stubs.TestConstants.PLAN_REQUEST
import stubs.TestConstants.PLAN_PRICE
import stubs.TestConstants.PLAN_PROCESSOR
import stubs.TestConstants.REQUEST_ID
import stubs.TestConstants.PLAN_TITLE
import stubs.TestConstants.PLAN_VISIBILITY
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
        PLAN_PROCESSOR.exec(ctx)
        assertEquals(PlanStubs.PLAN1.id, ctx.planResponse.id)
        assertEquals(PLAN_TITLE, ctx.planResponse.title)
        assertEquals(PLAN_CONDITIONS, ctx.planResponse.conditions)
        assertEquals(PLAN_DURATION, ctx.planResponse.duration)
        assertEquals(PLAN_PRICE, ctx.planResponse.price)
        assertEquals(PLAN_VISIBILITY, ctx.planResponse.visibility)
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
        PLAN_PROCESSOR.exec(ctx)
        assertEquals(PlanStubs.PLAN1.id, ctx.planResponse.id)
        assertEquals(PLAN_TITLE, ctx.planResponse.title)
        assertEquals(PLAN_CONDITIONS, ctx.planResponse.conditions)
        assertEquals(PLAN_DURATION, ctx.planResponse.duration)
        assertEquals(PLAN_PRICE, ctx.planResponse.price)
        assertEquals(PLAN_VISIBILITY, ctx.planResponse.visibility)
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
        PLAN_PROCESSOR.exec(ctx)
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
        PLAN_PROCESSOR.exec(ctx)
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
        PLAN_PROCESSOR.exec(ctx)
        val expectedPlan = PlanStubs.PLAN1
        assertEquals(planId, ctx.planResponse.id)
        assertEquals(expectedPlan.title, ctx.planResponse.title)
        assertEquals(expectedPlan.conditions, ctx.planResponse.conditions)
        assertEquals(expectedPlan.duration, ctx.planResponse.duration)
        assertEquals(expectedPlan.price, ctx.planResponse.price)
        assertEquals(expectedPlan.visibility, ctx.planResponse.visibility)
    }

}