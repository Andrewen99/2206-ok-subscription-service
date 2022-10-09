package stubs

import SubscriptionStubs
import contexts.SubscriptionContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.subscription.SubscriptionCommand
import stubs.TestConstants.BUY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.PLAN_ID
import stubs.TestConstants.READ_PAY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.REQUEST_ID
import stubs.TestConstants.SEARCH_SUBSCRIPTION_REQUEST
import stubs.TestConstants.SUB_PROCESSOR
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionSuccessTest {

    @Test
    fun buy() = runTest {
        val ctx = SubscriptionContext(
            command = SubscriptionCommand.BUY,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            subscriptionRequest = BUY_SUBSCRIPTION_REQUEST
        )
        val expectedSub = SubscriptionStubs.SUBSCRIPTION2
        SUB_PROCESSOR.exec(ctx)
        assertEquals(PLAN_ID, ctx.subscriptionResponse.planId)
        assertEquals(expectedSub.id, ctx.subscriptionResponse.id)
        assertEquals(expectedSub.startDate, ctx.subscriptionResponse.startDate)
        assertEquals(expectedSub.endDate, ctx.subscriptionResponse.endDate)
        assertEquals(expectedSub.isActive, ctx.subscriptionResponse.isActive)
        assertEquals(expectedSub.paymentStatus, ctx.subscriptionResponse.paymentStatus)
    }

    @Test
    fun pay() = runTest {
        val ctx = SubscriptionContext(
            command = SubscriptionCommand.PAY,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            subscriptionRequest = READ_PAY_SUBSCRIPTION_REQUEST
        )
        val expectedSub = SubscriptionStubs.SUBSCRIPTION1
        SUB_PROCESSOR.exec(ctx)
        assertEquals(expectedSub.id, ctx.subscriptionResponse.id)
        assertEquals(expectedSub.startDate, ctx.subscriptionResponse.startDate)
        assertEquals(expectedSub.endDate, ctx.subscriptionResponse.endDate)
        assertEquals(expectedSub.isActive, true)
        assertEquals(expectedSub.paymentStatus, ctx.subscriptionResponse.paymentStatus)
    }

    @Test
    fun read() = runTest {
        val ctx = SubscriptionContext(
            command = SubscriptionCommand.READ,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            subscriptionRequest = READ_PAY_SUBSCRIPTION_REQUEST
        )
        val expectedSub = SubscriptionStubs.SUBSCRIPTION1
        SUB_PROCESSOR.exec(ctx)
        assertEquals(expectedSub.id, ctx.subscriptionResponse.id)
        assertEquals(expectedSub.startDate, ctx.subscriptionResponse.startDate)
        assertEquals(expectedSub.endDate, ctx.subscriptionResponse.endDate)
        assertEquals(expectedSub.isActive, true)
        assertEquals(expectedSub.paymentStatus, ctx.subscriptionResponse.paymentStatus)
    }

    @Test
    fun search() = runTest {
        val ctx = SubscriptionContext(
            command = SubscriptionCommand.SEARCH,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.SUCCESS,
            requestId = REQUEST_ID,
            subscriptionFilter = SEARCH_SUBSCRIPTION_REQUEST
        )
        val expectedSub = SubscriptionStubs.SUBSCRIPTION1
        SUB_PROCESSOR.exec(ctx)
        assertEquals(expectedSub.id, ctx.subscriptionResponses[0].id)
        assertEquals(expectedSub.startDate, ctx.subscriptionResponses[0].startDate)
        assertEquals(expectedSub.endDate, ctx.subscriptionResponses[0].endDate)
        assertEquals(expectedSub.isActive, true)
    }
}