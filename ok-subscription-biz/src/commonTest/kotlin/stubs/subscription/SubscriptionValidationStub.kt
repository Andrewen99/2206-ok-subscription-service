package stubs.subscription

import contexts.SubscriptionContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionFilter
import stubs.TestConstants
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
fun subscriptionStubBadId(command: SubscriptionCommand, subRequest: Subscription?, filterRequest: SubscriptionFilter?) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.BAD_ID,
        requestId = TestConstants.REQUEST_ID,
    ).apply {
            filterRequest?.let { subscriptionFilter = it }
            subRequest?.let { subscriptionRequest = it }
    }
    TestConstants.SUB_PROCESSOR.exec(ctx)
    assertEquals(Subscription(), ctx.subscriptionResponse)
    assertTrue(ctx.subscriptionResponses.isEmpty())
    assertEquals("validation", ctx.errors[0].group)
    assertEquals("id", ctx.errors[0].field)
    assertEquals("validation-id", ctx.errors[0].code)
}

fun subscriptionStubDbError(command: SubscriptionCommand, subRequest: Subscription?, filterRequest: SubscriptionFilter?) = runTest {
    val ctx = SubscriptionContext(
        command = command,
        state = SbscrState.NONE,
        workMode = SbscrWorkMode.STUB,
        stubCase = SbscrStubs.DB_ERROR,
        requestId = TestConstants.REQUEST_ID,
    ).apply {
        filterRequest?.let { subscriptionFilter = it }
        subRequest?.let { subscriptionRequest = it }
    }
    TestConstants.SUB_PROCESSOR.exec(ctx)
    assertEquals(Subscription(), ctx.subscriptionResponse)
    assertTrue(ctx.subscriptionResponses.isEmpty())
    assertEquals("internal", ctx.errors[0].group)
    assertEquals("internal-db", ctx.errors[0].code)
}