import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

//TODO: подключить kotest и полноценно сравнить json
class ResponseSerializationTest {
    private val response = PlanCreateResponse(
        requestId = "12345",
        result = ResponseResult.SUCCESS,
        plan = PlanResponseObject(
            title = "3 month subscription",
            duration = 3,
            price = "10000",
            visibility = PlanVisibility.PUBLIC
        )
    )


    @Test
    fun `test serialization`() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"requestId\":\\s*\"12345\""))
        assertContains(json, Regex("\"title\":\\s*\"3 month subscription\""))
        assertContains(json, Regex("\"duration\":\\s*3"))

    }

    @Test
    fun `test deserialization`() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as PlanCreateResponse

        assertEquals(response, obj)
    }
}