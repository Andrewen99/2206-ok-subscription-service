import org.junit.Test
import kotlin.test.assertEquals

/**
 * Класс с примерами тестов для выполнения пункта 4
 * > **Подключить фреймворки для тестирования в подпроекте и сделать примеры тестов.**
 */
class ExampleTest {
    @Test
    fun `default params test`() {
        val dataWithOneDefaultField = TestData(10)
        assertEquals(3, dataWithOneDefaultField.changeableField)
    }

    @Test
    fun `changeable field test`() {
        val data = TestData(10, 15)
        assertEquals(15, data.changeableField)
        data.changeableField = data.changeableField * 5
        assertEquals(75, data.changeableField)
    }
}