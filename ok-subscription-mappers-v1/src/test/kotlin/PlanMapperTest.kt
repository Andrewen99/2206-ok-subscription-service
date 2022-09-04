import contexts.PlanContext
import models.*
import models.plan.Plan
import models.plan.PlanCommand
import models.plan.SbscrPlanVisibility
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import toTransport.toTransportPlan
import toTransport.toTransportSubscription
import kotlin.test.assertEquals

class PlanMapperTest {
    @Test
    fun fromTransport() {
        val req = PlanCreateRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            plan = PlanCreateObject(
                title = "title",
                conditions = setOf("condition #1", "condition #2"),
                duration = 5,
                price = "950",
                visibility = PlanVisibility.PUBLIC
            )
        )
        val context = PlanContext()
        context.fromTransport(req)

        assertEquals(SbscrStubs.SUCCESS, context.stubCase)
        assertEquals(SbscrWorkMode.STUB, context.workMode)

        assertEquals("title", context.planRequest.title)
        assertEquals(setOf("condition #1", "condition #2"), context.planRequest.conditions)
        assertEquals(5, context.planRequest.duration)
        assertEquals("950", context.planRequest.price)
        assertEquals(SbscrPlanVisibility.PUBLIC, context.planRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = PlanContext(
            requestId = SbscrRequestId("1234"),
            command = PlanCommand.CREATE,
            planResponse = Plan(
                title = "title",
                conditions = mutableSetOf("condition #1", "condition #2"),
                duration = 5,
                price = "950",
                visibility = SbscrPlanVisibility.PUBLIC
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

        val req = context.toTransportPlan() as PlanCreateResponse

        assertEquals("1234", req.requestId)

        assertEquals("title", req.plan?.title)
        assertEquals(setOf("condition #1", "condition #2"), req.plan?.conditions)
        assertEquals(5, req.plan?.duration)
        assertEquals("950", req.plan?.price)
        assertEquals(PlanVisibility.PUBLIC, req.plan?.visibility)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}