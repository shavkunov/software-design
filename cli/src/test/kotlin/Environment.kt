import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.utilities.Cat
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class EnvironmentTest {
    @Test
    fun addVariable() {
        val environment = Environment()
        environment.setVariable("a", "b")

        assertEquals("b", environment.getVariable("a"))
        assertEquals("", environment.getVariable("c"))
    }

    @Test
    fun addUtility() {
        val environment = Environment()
        environment.addUtility("cat", Cat)

        val result = environment.executeCommand("cat", emptyList(), "123")
        assertFalse(result.isExit)
        assertEquals("123", result.output)
    }

    @Test(expected = IOException::class)
    fun testOuterCommand() {
        val environment = Environment()

        val result = environment.executeCommand("echo", listOf("123"), "")
        assertFalse(result.isExit)
        assertEquals("123\n", result.output)

        environment.executeCommand("olololo", emptyList(), "123")
    }
}