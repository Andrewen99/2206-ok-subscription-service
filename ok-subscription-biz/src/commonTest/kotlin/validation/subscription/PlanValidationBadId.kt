package validation.subscription

import SubscriptionProcessor
import contexts.PlanContext
import contexts.SubscriptionContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import models.SbscrPrincipalModel
import models.SbscrState
import models.SbscrUserId
import models.SbscrWorkMode
import models.plan.Plan
import models.plan.PlanCommand
import models.plan.PlanId
import models.plan.SbscrPlanVisibility
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import permissions.SbscrUserGroups
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPlanIdCorrect(command: SubscriptionCommand, processor: SubscriptionProcessor) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("owner-id-1"), groups = setOf(SbscrUserGroups.ADMIN)),
        subscriptionRequest = Subscription(
            id = SubscriptionId(id = "123-subscription-456-XYZ"),
            ownerId = SbscrUserId(id = "123-USER-123"),
            planId = PlanId(id = "123-plan-456-XYZ"),
            startDate = LocalDate(2022, 10, 15),
            endDate =LocalDate(2024, 5, 15),
            isActive = true,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals("123-subscription-456-XYZ", ctx.subscriptionValidated.id.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPlanIdTrim(command: SubscriptionCommand, processor: SubscriptionProcessor) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("owner-id-1"), groups = setOf(SbscrUserGroups.ADMIN)),
        subscriptionRequest = Subscription(
            id = SubscriptionId(id = "123-subscription-456-XYZ"),
            ownerId = SbscrUserId(id = "123-USER-123"),
            planId = PlanId(id = "\n\t 123-plan-456-XYZ \n\t"),
            startDate = LocalDate(2022, 10, 15),
            endDate =LocalDate(2024, 5, 15),
            isActive = true,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(SbscrState.FAILING, ctx.state)
    assertEquals("123-subscription-456-XYZ", ctx.subscriptionValidated.id.asString())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPlanIdEmpty(command: SubscriptionCommand, processor: SubscriptionProcessor) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("owner-id-1"), groups = setOf(SbscrUserGroups.ADMIN)),
        subscriptionRequest = Subscription(
            id = SubscriptionId(id = "123-subscription-456-XYZ"),
            ownerId = SbscrUserId(id = "123-USER-123"),
            planId = PlanId(id = ""),
            startDate = LocalDate(2022, 10, 15),
            endDate =LocalDate(2024, 5, 15),
            isActive = true,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("planId", error?.field)
    assertContains(error?.code ?: "", "empty")
    assertContains(error?.message ?: "", "planId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationPlanIdFormat(command: SubscriptionCommand, processor: SubscriptionProcessor) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode =  SbscrWorkMode.TEST,
        principal = SbscrPrincipalModel(id = SbscrUserId("owner-id-1"), groups = setOf(SbscrUserGroups.ADMIN)),
        subscriptionRequest = Subscription(
            id = SubscriptionId(id = "123-subscription-456-XYZ"),
            ownerId = SbscrUserId(id = "123-USER-123"),
            planId = PlanId(id = "!@#\$%^&*(),.{}"),
            startDate = LocalDate(2022, 10, 15),
            endDate =LocalDate(2024, 5, 15),
            isActive = true,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(SbscrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("planId", error?.field)
    assertContains(error?.code ?: "", "badFormat")
    assertContains(error?.message ?: "", "planId")
}