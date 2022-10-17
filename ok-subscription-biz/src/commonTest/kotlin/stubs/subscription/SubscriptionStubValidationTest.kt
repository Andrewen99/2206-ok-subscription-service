package stubs.subscription

import contexts.SubscriptionContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.subscription.Subscription
import models.subscription.SubscriptionCommand.*
import stubs.TestConstants
import stubs.TestConstants.BUY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.READ_PAY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.SEARCH_SUBSCRIPTION_REQUEST
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionStubValidationTest {

    @Test fun readBadId() = subscriptionStubBadId(READ, READ_PAY_SUBSCRIPTION_REQUEST, null)
    @Test fun payBadId() = subscriptionStubBadId(PAY, READ_PAY_SUBSCRIPTION_REQUEST, null)
    @Test fun buyBadId() = subscriptionStubBadId(BUY, BUY_SUBSCRIPTION_REQUEST, null)
    @Test fun searchBadId() = subscriptionStubBadId(SEARCH, null, SEARCH_SUBSCRIPTION_REQUEST)

    @Test fun readDbError() = subscriptionStubDbError(READ, READ_PAY_SUBSCRIPTION_REQUEST, null)
    @Test fun payDbError() = subscriptionStubDbError(PAY, READ_PAY_SUBSCRIPTION_REQUEST, null)
    @Test fun buyDbError() = subscriptionStubDbError(BUY, BUY_SUBSCRIPTION_REQUEST, null)
    @Test fun searchDbError() = subscriptionStubDbError(SEARCH, null, SEARCH_SUBSCRIPTION_REQUEST)

    @Test
    fun cannotBuy() = runTest {
            val ctx = SubscriptionContext(
                command = BUY,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.CANNOT_BUY,
                requestId = TestConstants.REQUEST_ID,
                subscriptionRequest = BUY_SUBSCRIPTION_REQUEST
            )
            TestConstants.SUB_PROCESSOR.exec(ctx)
            assertEquals(Subscription(), ctx.subscriptionResponse)
            assertEquals("error", ctx.errors[0].group)
            assertEquals("error-cannot-buy", ctx.errors[0].code)
    }

    @Test
    fun paymentError() = runTest {
        val ctx = SubscriptionContext(
            command = PAY,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.PAYMENT_ERROR,
            requestId = TestConstants.REQUEST_ID,
            subscriptionRequest = BUY_SUBSCRIPTION_REQUEST
        )
        TestConstants.SUB_PROCESSOR.exec(ctx)
        assertEquals(Subscription(), ctx.subscriptionResponse)
        assertEquals("error", ctx.errors[0].group)
        assertEquals("error-unsuccessful-payment", ctx.errors[0].code)
    }

    @Test
    fun badSearchParameters() = runTest {
        val ctx = SubscriptionContext(
            command = SEARCH,
            state = SbscrState.NONE,
            workMode = SbscrWorkMode.STUB,
            stubCase = SbscrStubs.BAD_SEARCH_PARAMETERS,
            requestId = TestConstants.REQUEST_ID,
            subscriptionRequest = BUY_SUBSCRIPTION_REQUEST
        )
        TestConstants.SUB_PROCESSOR.exec(ctx)
        assertEquals(Subscription(), ctx.subscriptionResponse)
        assertEquals("validation", ctx.errors[0].group)
        assertEquals("boughtPeriod", ctx.errors[0].field)
        assertEquals("validation-search-parameters", ctx.errors[0].code)
    }
}