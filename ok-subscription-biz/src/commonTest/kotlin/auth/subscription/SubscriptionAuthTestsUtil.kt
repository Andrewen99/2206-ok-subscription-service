package auth.subscription

import SubscriptionProcessor
import contexts.PlanContext
import contexts.SubscriptionContext
import kotlinx.datetime.LocalDate
import models.SbscrPrincipalModel
import models.SbscrState
import models.SbscrUserId
import models.SbscrWorkMode
import models.plan.*
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import permissions.SbscrUserGroups
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

suspend fun execSubscriptionProcessor(command: SubscriptionCommand, processor: SubscriptionProcessor, principalModel: SbscrPrincipalModel, isOwner : Boolean = true): SubscriptionContext {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        principal = principalModel,
        subscriptionRequest = Subscription(
            id = SubscriptionId(id = "123-subscription-456-XYZ"),
            ownerId = if (isOwner) principalModel.id else SbscrUserId(id = "123-USER-123"),
            planId = PlanId(id = "123-plan-456-XYZ"),
            startDate = LocalDate(2022, 10, 15),
            endDate = LocalDate(2024, 5, 15),
            isActive = true,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    )

    processor.exec(ctx)
    return ctx
}

fun assertAllowed(ctx: SubscriptionContext) {
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
}

fun assertNotAllowed(ctx: SubscriptionContext) {
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("User is not allowed to perform this operation on subscription", error?.message)
}