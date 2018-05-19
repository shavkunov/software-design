package parsers

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.interpreter.BuiltinCommandParser
import ru.spbau.shavkunov.utilities.Cat
import ru.spbau.shavkunov.utilities.Echo
import java.io.IOException
import kotlin.test.assertEquals

class BuiltinCommandTest {
    @Test
    fun test() {
        val env = Environment()
        env.addUtility("echo", Echo)
        env.addUtility("myUtility", Echo)

        val text = createEscapedString("echo arg1 arg2 arg3")
        val anotherText = createEscapedString("myUtility arg1 arg2")
        val operator = BuiltinCommandParser.parse(text)
        val anotherOperator = BuiltinCommandParser.parse(anotherText)

        var result = operator.execute("", env)
        assertEquals("arg1 arg2 arg3\n", result.output)

        result = operator.execute("asdff", env)
        assertEquals("arg1 arg2 arg3\n", result.output)

        result = anotherOperator.execute("", env)
        assertEquals("arg1 arg2\n", result.output)

        result = anotherOperator.execute("asdff", env)
        assertEquals("arg1 arg2\n", result.output)
    }

    @Test
    fun noUtilityTest() {
        val env = Environment()
        env.addUtility("echo", Echo)

        var text = createEscapedString("echo arg1 arg2 arg3")
        var operator = BuiltinCommandParser.parse(text)
        val result = operator.execute("", env)
        assertEquals("arg1 arg2 arg3\n", result.output)

        text = createEscapedString("unknown_command arg1 arg2 arg3")
        operator = BuiltinCommandParser.parse(text)
        val failedResult = operator.execute("", env)
        val error = "Command unknown_command failed: Cannot run program \"unknown_command\": " +
                "error=2, No such file or directory"
        assertEquals(error, failedResult.output)
    }
}