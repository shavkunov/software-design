package utilities

import org.junit.Test
import ru.spbau.shavkunov.utilities.Echo
import kotlin.test.assertEquals

class EchoTest {
    @Test
    fun test() {
        val result = Echo.execute(listOf("123"), "")

        assertEquals("123\n", result.output)
        assertEquals(false, result.isExit)
    }
}