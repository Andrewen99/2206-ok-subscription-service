import contexts.SbscrContext
import models.*
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportSubscription
import kotlin.test.assertEquals

class SbscrMapperTest {
    @Test
    fun fromTransport() {
        val req = SubscriptionCreateRequest(
            requestId = "1234",
            debug = SubscriptionDebug(
                mode = SubscriptionRequestDebugMode.STUB,
                stub = SubscriptionRequestDebugStubs.SUCCESS
            ),
            subscription = SubscriptionCreateObject(
                title = "title",
                conditions = setOf("condition #1", "condition #2"),
                duration = 5,
                price = "950",
                visibility = SubscriptionVisibility.PUBLIC
            )
        )
        val context = SbscrContext()
        context.fromTransport(req)

        assertEquals(SbscrStubs.SUCCESS, context.stubCase)
        assertEquals(SbscrWorkMode.STUB, context.workMode)

        assertEquals("title", context.sbscrRequest.title)
        assertEquals(setOf("condition #1", "condition #2"), context.sbscrRequest.conditions)
        assertEquals(5, context.sbscrRequest.duration)
        assertEquals("950", context.sbscrRequest.price)
        assertEquals(SbscrVisibility.PUBLIC, context.sbscrRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = SbscrContext().apply {
            requestId = SbscrRequestId("1234")
            command = SbscrCommand.CREATE
            sbscrResponse = Subscription(
                title = "title",
                conditions = mutableSetOf("condition #1", "condition #2"),
                duration = 5,
                price = "950",
                visibility = SbscrVisibility.PUBLIC
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

        val req = context.toTransportSubscription() as SubscriptionCreateResponse

        assertEquals("1234", req.requestId)

        assertEquals("title", req.subscription?.title)
        assertEquals(setOf("condition #1", "condition #2"), req.subscription?.conditions)
        assertEquals(5, req.subscription?.duration)
        assertEquals("950", req.subscription?.price)
        assertEquals(SubscriptionVisibility.PUBLIC, req.subscription?.visibility)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}