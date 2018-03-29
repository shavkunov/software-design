package parsers

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.interpreter.AssignmentParser
import ru.spbau.shavkunov.interpreter.BuiltinCommandParser
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Assignment {
    @Test
    fun testAssignment() {
        val line = createEscapedStringWithAssign()

        val operator = AssignmentParser.parse(line)!!
        val env = Environment()

        operator.execute("", env)
        assertEquals("wifi", env.getVariable("ololo"))
    }

    @Test
    fun testNoAssignment() {
        val line = createEscapedString()

        val operator = AssignmentParser.parse(line)
        assertNull(operator)
    }

    @Test
    fun testWithCommand() {
        val line = createEscapedString("echo=1234")
        val env = Environment()

        val operator = AssignmentParser.parse(line)!!
        operator.execute("", env)

        val command = createEscapedString("echo \$echo")
        val builtinOperator = BuiltinCommandParser.parse(command)
        val result = builtinOperator.execute("", env)

        assertEquals("1234\n", result.output)
    }
}