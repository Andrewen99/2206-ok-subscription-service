package stubs

import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.plan.Plan
import models.plan.PlanCommand
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanStubValidationTest {

    @Test
    fun badTitle() = runTest {
        listOf(PlanCommand.CREATE, PlanCommand.UPDATE).forEach { command ->
            val ctx = PlanContext(
                command = command,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.BAD_TITLE,
                requestId = TestConstants.REQUEST_ID,
                planRequest = TestConstants.PLAN_REQUEST
            )
            TestConstants.PROCESSOR.exec(ctx)
            assertEquals(Plan(), ctx.planResponse)
            assertEquals("validation", ctx.errors[0].group)
            assertEquals("title", ctx.errors[0].field)
            assertEquals("validation-title", ctx.errors[0].code)
        }
    }

    @Test
    fun badCondition() = runTest {
        listOf(PlanCommand.CREATE, PlanCommand.UPDATE).forEach { command ->
            val ctx = PlanContext(
                command = command,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.BAD_CONDITION,
                requestId = TestConstants.REQUEST_ID,
                planRequest = TestConstants.PLAN_REQUEST
            )
            TestConstants.PROCESSOR.exec(ctx)
            assertEquals(Plan(), ctx.planResponse)
            assertEquals("validation", ctx.errors[0].group)
            assertEquals("condition", ctx.errors[0].field)
            assertEquals("validation-condition", ctx.errors[0].code)
        }
    }

    @Test
    fun dbError() = runTest {
        listOf(PlanCommand.CREATE, PlanCommand.UPDATE, PlanCommand.READ, PlanCommand.READ_ALL, PlanCommand.DELETE).forEach { command ->
            val ctx = PlanContext(
                command = command,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.DB_ERROR,
                requestId = TestConstants.REQUEST_ID,
                planRequest = TestConstants.PLAN_REQUEST
            )
            TestConstants.PROCESSOR.exec(ctx)
            assertEquals(Plan(), ctx.planResponse)
            assertEquals("internal", ctx.errors[0].group)
            assertEquals("internal-db", ctx.errors[0].code)
        }
    }

    @Test
    fun badId() = runTest {
        listOf(PlanCommand.UPDATE, PlanCommand.READ, PlanCommand.DELETE).forEach { command ->
            val ctx = PlanContext(
                command = command,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.BAD_ID,
                requestId = TestConstants.REQUEST_ID,
                planRequest = TestConstants.PLAN_REQUEST
            )
            TestConstants.PROCESSOR.exec(ctx)
            assertEquals(Plan(), ctx.planResponse)
            assertEquals("validation", ctx.errors[0].group)
            assertEquals("id", ctx.errors[0].field)
            assertEquals("validation-id", ctx.errors[0].code)
        }
    }
}