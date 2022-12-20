package auth.plan

import PlanProcessor
import contexts.PlanContext
import models.SbscrPrincipalModel
import models.SbscrState
import models.SbscrWorkMode
import models.plan.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

suspend fun execPlanProcessor(command: PlanCommand, processor: PlanProcessor, principalModel: SbscrPrincipalModel): PlanContext {
    val ctx = PlanContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.TEST,
        principal = principalModel,
        planRequest = Plan(
            id = PlanId("123"),
            lock = PlanLock("123-abc-456-XYZ"),
            title = "abc",
            conditions = mutableSetOf("condition#1", "condition#2", "condition#3"),
            duration = 5,
            price = "10",
            visibility = SbscrPlanVisibility.PUBLIC
        )
    )
    processor.exec(ctx)
    return ctx
}

fun assertAllowed(ctx: PlanContext) {
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
}

fun assertNotAllowed(ctx: PlanContext) {
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("User is not allowed to perform this operation on plan", error?.message)
}