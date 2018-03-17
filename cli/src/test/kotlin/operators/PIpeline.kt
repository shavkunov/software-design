package operators

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.commands.AssignmentOperator
import ru.spbau.shavkunov.commands.BuiltinOperator
import ru.spbau.shavkunov.commands.PipelineOperator
import ru.spbau.shavkunov.interpreter.SimpleToken
import ru.spbau.shavkunov.utilities.Cat
import kotlin.test.assertEquals

class PIpelineTest {
    @Test
    fun test() {
        val environment = Environment()
        environment.addUtility("cat", Cat)

        val left = BuiltinOperator(SimpleToken("echo"),
                listOf(SimpleToken("a"), SimpleToken("b")))

        val right = BuiltinOperator(SimpleToken("cat"), emptyList())

        val operator = PipelineOperator(left, right)
        val result = operator.execute("", environment)

        assertEquals("a b\n", result.output)
    }
}