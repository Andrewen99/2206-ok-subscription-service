package stubs

import contexts.SubscriptionContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.SbscrState
import models.SbscrStubs
import models.SbscrWorkMode
import models.subscription.Subscription
import models.subscription.SubscriptionCommand.*
import models.subscription.SubscriptionFilter
import stubs.TestConstants.BUY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.READ_PAY_SUBSCRIPTION_REQUEST
import stubs.TestConstants.SEARCH_SUBSCRIPTION_REQUEST
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionStubValidationTest {
    private val commandToRequestList = listOf(
        Pair(READ,READ_PAY_SUBSCRIPTION_REQUEST),
        Pair(PAY,READ_PAY_SUBSCRIPTION_REQUEST),
        Pair(BUY,BUY_SUBSCRIPTION_REQUEST),
        Pair(SEARCH,SEARCH_SUBSCRIPTION_REQUEST)
    )

    @Test
    fun badId() = runTest {
        commandToRequestList.forEach { commandToRequest ->
            val ctx = SubscriptionContext(
                command = commandToRequest.first,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.BAD_ID,
                requestId = TestConstants.REQUEST_ID,
            ).apply {
                if (commandToRequest.first == SEARCH) {
                    subscriptionFilter = commandToRequest.second as SubscriptionFilter
                } else {
                    subscriptionRequest = commandToRequest.second as Subscription
                }
            }
            TestConstants.SUB_PROCESSOR.exec(ctx)
            assertEquals(Subscription(), ctx.subscriptionResponse)
            assertTrue(ctx.subscriptionResponses.isEmpty())
            assertEquals("validation", ctx.errors[0].group)
            assertEquals("id", ctx.errors[0].field)
            assertEquals("validation-id", ctx.errors[0].code)
        }
    }

    @Test
    fun dbError() = runTest {
        commandToRequestList.forEach { commandToRequest ->
            val ctx = SubscriptionContext(
                command = commandToRequest.first,
                state = SbscrState.NONE,
                workMode = SbscrWorkMode.STUB,
                stubCase = SbscrStubs.DB_ERROR,
                requestId = TestConstants.REQUEST_ID,
            ).apply {
                if (commandToRequest.first == SEARCH) {
                    subscriptionFilter = commandToRequest.second as SubscriptionFilter
                } else {
                    subscriptionRequest = commandToRequest.second as Subscription
                }
            }
            TestConstants.SUB_PROCESSOR.exec(ctx)
            assertEquals(Subscription(), ctx.subscriptionResponse)
            assertTrue(ctx.subscriptionResponses.isEmpty())
            assertEquals("internal", ctx.errors[0].group)
            assertEquals("internal-db", ctx.errors[0].code)
        }
    }

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