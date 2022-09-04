import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

//TODO: подключить kotest и полноценно сравнить json
class RequestSerializationTest {
    private val request = PlanCreateRequest(
        requestId = "12345",
        debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.BAD_TITLE
        ),
        plan = PlanCreateObject(
            title = "3 month subscription",
            duration = 3,
            price = "10000",
            visibility = PlanVisibility.PUBLIC
        )
    )

    @Test
    fun `test serialization`() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"3 month subscription\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"duration\":\\s*3"))
        assertContains(json, Regex("\"requestType\":\\s*\"planCreate\""))
    }

    @Test
    fun `test deserialization`() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as PlanCreateRequest

        assertEquals(request, obj)
    }

//    private val request = AdCreateRequest(
//        requestId = "123",
//        debug = AdDebug(
//            mode = AdRequestDebugMode.STUB,
//            stub = AdRequestDebugStubs.BAD_TITLE
//        ),
//        ad = AdCreateObject(
//            title = "ad title",
//            description = "ad description",
//            adType = DealSide.DEMAND,
//            visibility = AdVisibility.PUBLIC,
//        )
//    )
//
//    @Test
//    fun serialize() {
//        val json = apiV1Mapper.writeValueAsString(request)
//
//        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
//        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
//        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
//        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
//    }
}