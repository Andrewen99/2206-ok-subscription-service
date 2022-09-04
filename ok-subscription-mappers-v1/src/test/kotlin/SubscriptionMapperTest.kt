import contexts.SubscriptionContext
import models.*
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportSubscription
import java.time.LocalDate
import kotlin.test.assertEquals

class SubscriptionMapperTest {
    @Test
    fun fromTransport() {
        val req = PlanBuyRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            plan = PlanBuyObject(
                id = "subId123"
            )
        )
        val context = SubscriptionContext()
        context.fromTransport(req)

        assertEquals(SbscrStubs.SUCCESS, context.stubCase)
        assertEquals(SbscrWorkMode.STUB, context.workMode)
        assertEquals(PlanId("subId123"), context.planId)
    }

    @Test
    fun toTransport() {
        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusMonths(5)
        val context = SubscriptionContext(
            requestId = SbscrRequestId("1234"),
            command = SubscriptionCommand.BUY,
            subscriptionResponse = Subscription(
                id = SubscriptionId("subscriptionId789"),
                subscriptionId = PlanId("planId123"),
                startDate = startDate,
                endDate = endDate,
                isActive = true
            ),
            errors = mutableListOf(
                SbscrError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = SbscrState.RUNNING
        )

        val req = context.toTransportSubscription() as PlanBuyResponse

        assertEquals("1234", req.requestId)

        assertEquals("subscriptionId789", req.subscription?.id)
        assertEquals("planId123", req.subscription?.planId)
        assertEquals(startDate.format(DATE_FORMATTER), req.subscription?.startDate)
        assertEquals(endDate.format(DATE_FORMATTER), req.subscription?.endDate)
        assertEquals(true, req.subscription?.isActive)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}