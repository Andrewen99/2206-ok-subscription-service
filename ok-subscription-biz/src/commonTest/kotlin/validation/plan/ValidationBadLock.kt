package validation.plan

import PlanProcessor
import contexts.PlanContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrPrincipalModel
import models.SbscrState
import models.SbscrUserId
import models.SbscrWorkMode
import models.plan.*
import permissions.SbscrUserGroups
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockCorrect(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("123"), groups = setOf(SbscrUserGroups.ADMIN)),
        planRequest = Plan(
            id = PlanId("123-abc-456-XYZ"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("condition#1", "condition#2", "condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals("123-abc-456-XYZ", ctx.planValidated.lock.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockTrim(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("123"), groups = setOf(SbscrUserGroups.ADMIN)),
        planRequest = Plan(
            id = PlanId("123-abc-456-XYZ"),
            lock = PlanLock("\n\t 123-abc-456-XYZ \n\t"),
            title = "abc",
            conditions = mutableSetOf("condition#1", "condition#2", "condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals("123-abc-456-XYZ", ctx.planValidated.lock.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockEmpty(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("123"), groups = setOf(SbscrUserGroups.ADMIN)),
        planRequest = Plan(
            id = PlanId("123-abc-456-XYZ"),
            lock = PlanLock(""),
            title = "abc",
            conditions = mutableSetOf("condition#1", "condition#2", "condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.code ?: "", "empty")
    assertContains(error?.message ?: "", "lock")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockFormat(command: PlanCommand, processor: PlanProcessor) = runTest {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("123"), groups = setOf(SbscrUserGroups.ADMIN)),
        planRequest = Plan(
            id = PlanId("123-abc-456-XYZ"),
            lock = PlanLock("!@#\$%^&*(),.{}"),
            title = "abc",
            conditions = mutableSetOf("condition#1", "condition#2", "condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.code ?: "", "badFormat")
    assertContains(error?.message ?: "", "lock")
}