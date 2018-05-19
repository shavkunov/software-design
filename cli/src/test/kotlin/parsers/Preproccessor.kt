package parsers

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.commands.AssignmentOperator
import ru.spbau.shavkunov.interpreter.*
import ru.spbau.shavkunov.utilities.Echo
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreproccessorTest {
    @Test
    fun testCommonString() {
        val text = "echo 12 24 51"
        val preprocessor = Preprocessor()

        val result = preprocessor.process(text)

        for (char: EscapeChar in result) {
            assertFalse(char.isEscaped)
        }

        assertEquals(text, createString(result))
    }

    @Test
    fun testWeakEscape() {
        val text = "echo \"12 24 51\""
        val preprocessor = Preprocessor()

        val result = preprocessor.process(text)
        for ((index, char: EscapeChar) in result.withIndex()) {
            if (index <= 5 || index == 15) {
                assertFalse(char.isEscaped)
            } else {
                assertTrue(char.isEscaped)
            }
        }
    }

    @Test
    fun testStrongEscape() {
        val text = "echo \'12 24 51\'"
        val preprocessor = Preprocessor()

        val result = preprocessor.process(text)
        for ((index, char: EscapeChar) in result.withIndex()) {
            if (index <= 5 || index == 15) {
                assertFalse(char.isEscaped)
            } else {
                assertTrue(char.isEscaped)
            }
        }
    }

    @Test
    fun testWeakWithCommand() {
        val text = "echo \"text message \$expand\""

        val assignmentOperator = AssignmentOperator("expand", "value")
        val env = Environment()
        env.addUtility("echo", Echo)
        assignmentOperator.execute("", env)

        val operator = InputHandler.handle(text)
        val result = operator.execute("", env)

        assertEquals("text message value\n", result.output)
    }

    @Test
    fun testStrongWithCommand() {
        val text = "echo \'text message \$expand\'"

        val assignmentOperator = AssignmentOperator("expand", "value")
        val env = Environment()
        env.addUtility("echo", Echo)
        assignmentOperator.execute("", env)

        val operator = InputHandler.handle(text)
        val result = operator.execute("", env)

        assertEquals("text message \$expand\n", result.output)
    }
}