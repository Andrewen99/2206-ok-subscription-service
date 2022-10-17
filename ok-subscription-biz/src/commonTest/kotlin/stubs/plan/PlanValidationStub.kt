package stubs.plan

import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.plan.Plan
import models.plan.PlanCommand
import stubs.TestConstants
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun planStubBadTitle(command: PlanCommand) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.BAD_TITLE,
        requestId = TestConstants.REQUEST_ID,
        planRequest = TestConstants.PLAN_REQUEST
    )
    TestConstants.PLAN_PROCESSOR.exec(ctx)
    assertEquals(Plan(), ctx.planResponse)
    assertEquals("validation", ctx.errors[0].group)
    assertEquals("title", ctx.errors[0].field)
    assertEquals("validation-title", ctx.errors[0].code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun planStubBadConditions(command: PlanCommand) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.BAD_CONDITION,
        requestId = TestConstants.REQUEST_ID,
        planRequest = TestConstants.PLAN_REQUEST
    )
    TestConstants.PLAN_PROCESSOR.exec(ctx)
    assertEquals(Plan(), ctx.planResponse)
    assertEquals("validation", ctx.errors[0].group)
    assertEquals("condition", ctx.errors[0].field)
    assertEquals("validation-condition", ctx.errors[0].code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun planStubDbError(command: PlanCommand) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.DB_ERROR,
        requestId = TestConstants.REQUEST_ID,
        planRequest = TestConstants.PLAN_REQUEST
    )
    TestConstants.PLAN_PROCESSOR.exec(ctx)
    assertEquals(Plan(), ctx.planResponse)
    assertEquals("internal", ctx.errors[0].group)
    assertEquals("internal-db", ctx.errors[0].code)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun planStubBadId(command: PlanCommand) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.BAD_ID,
        requestId = TestConstants.REQUEST_ID,
        planRequest = TestConstants.PLAN_REQUEST
    )
    TestConstants.PLAN_PROCESSOR.exec(ctx)
    assertEquals(Plan(), ctx.planResponse)
    assertEquals("validation", ctx.errors[0].group)
    assertEquals("id", ctx.errors[0].field)
    assertEquals("validation-id", ctx.errors[0].code)
}