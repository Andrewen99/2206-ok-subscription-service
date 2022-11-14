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
fun validationPriceCorrect(command: PlanCommand, processor: PlanProcessor) = runTest {
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
    assertEquals("10", ctx.planValidated.price)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriceTrim(command: PlanCommand, processor: PlanProcessor) = runTest {
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
            price = "\n\t 10 \n\t",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals("10", ctx.planValidated.price)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriceEmpty(command: PlanCommand, processor: PlanProcessor) = runTest {
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
            price = "",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertContains(error?.code ?: "", "empty")
    assertContains(error?.message ?: "", "price")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriceSymbols(command: PlanCommand, processor: PlanProcessor) = runTest {
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
            price = "abcd",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertContains(error?.code ?: "", "badFormat")
    assertContains(error?.message ?: "", "price")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPriceNegative(command: PlanCommand, processor: PlanProcessor) = runTest {
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
            price = "-10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertContains(error?.code ?: "", "badFormat")
    assertContains(error?.message ?: "", "price")
}