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
fun validationConditionsCorrect(command: PlanCommand, processor: PlanProcessor) = runTest {
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
    assertEquals(mutableSetOf("condition#1","condition#2","condition#3"), ctx.planValidated.conditions)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationConditionsTrim(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("\n\t condition#1 \n\t", "\n\t condition#2 \n\t", "\n\t condition#3 \n\t"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals(mutableSetOf("condition#1","condition#2","condition#3"), ctx.planValidated.conditions)
}


@OptIn(ExperimentalCoroutinesApi::class)
fun validationConditionsEmpty(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf(),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("conditions", error?.field)
    assertContains(error?.code ?: "", "empty")
    assertContains(error?.message ?: "", "conditions")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationConditionsSymbols(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("!@#\$%^&*(),.{}","condition#2","condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("conditions", error?.field)
    assertContains(error?.code ?: "", "badFormat")
    assertContains(error?.message ?: "", "conditions")
}