package utilities

import org.junit.Test
import ru.spbau.shavkunov.utilities.Exit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExitTest {
    @Test
    fun test() {
        val result1 = Exit.execute(emptyList(), "")
        val result2 = Exit.execute(listOf("123", "1"), "")
        val result3 = Exit.execute(emptyList(), "123")

        assertTrue(result1.isExit)
        assertEquals("", result1.output)

        assertTrue(result2.isExit)
        assertEquals("", result2.output)

        assertTrue(result3.isExit)
        assertEquals("", result3.output)
    }
}