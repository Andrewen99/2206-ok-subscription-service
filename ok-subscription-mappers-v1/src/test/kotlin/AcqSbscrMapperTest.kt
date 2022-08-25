import contexts.AcqSbscrContext
import contexts.SbscrContext
import models.*
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportSubscription
import java.time.LocalDate
import kotlin.test.assertEquals

class AcqSbscrMapperTest {
    @Test
    fun fromTransport() {
        val req = SubscriptionBuyRequest(
            requestId = "1234",
            debug = SubscriptionDebug(
                mode = SubscriptionRequestDebugMode.STUB,
                stub = SubscriptionRequestDebugStubs.SUCCESS
            ),
            subscription = SubscriptionBuyObject(
                id = "subId123"
            )
        )
        val context = AcqSbscrContext()
        context.fromTransport(req)

        assertEquals(SbscrStubs.SUCCESS, context.stubCase)
        assertEquals(SbscrWorkMode.STUB, context.workMode)
        assertEquals(SbscrId("subId123"), context.subscriptionId)
    }

    @Test
    fun toTransport() {
        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusMonths(5)
        val context = AcqSbscrContext().apply {
            requestId = SbscrRequestId("1234")
            command = AcqSbscrCommand.BUY
            acqSbscrResponse = AcquiredSubscription(
                id = AcqSbscrId("acqSubId789"),
                subscriptionId = SbscrId("subId123"),
                startDate = startDate,
                endDate = endDate,
                isActive = true
            )
            errors = mutableListOf(
                SbscrError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            )
            state = SbscrState.RUNNING
        }

        val req = context.toTransportSubscription() as SubscriptionBuyResponse

        assertEquals("1234", req.requestId)

        assertEquals("acqSubId789", req.acquiredSubscription?.id)
        assertEquals("subId123", req.acquiredSubscription?.subscriptionId)
        assertEquals(startDate.format(DATE_FORMATTER), req.acquiredSubscription?.startDate)
        assertEquals(endDate.format(DATE_FORMATTER), req.acquiredSubscription?.endDate)
        assertEquals(true, req.acquiredSubscription?.isActive)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}