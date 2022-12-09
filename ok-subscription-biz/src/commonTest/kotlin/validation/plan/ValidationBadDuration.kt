package validation.plan

import PlanProcessor
import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrWorkMode
import models.plan.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDurationCorrect(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("condition#1","condition#2","condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals(5, ctx.planValidated.duration)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDurationZero(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("condition#1","condition#2","condition#3"),
            duration = 0,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("duration", error?.field)
    assertContains(error?.message ?: "", "duration")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDurationNegative(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("condition#1","condition#2","condition#3"),
            duration = -2,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("duration", error?.field)
    assertContains(error?.message ?: "", "duration")
}