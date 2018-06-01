package operators

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.commands.AssignmentOperator
import ru.spbau.shavkunov.commands.BuiltinOperator
import ru.spbau.shavkunov.createStandardEnvironment
import ru.spbau.shavkunov.interpreter.SimpleToken
import kotlin.test.assertEquals

class Builtin {
    @Test
    fun test() {
        val environment = createStandardEnvironment()
        val operator = BuiltinOperator(SimpleToken("echo"),
                listOf(SimpleToken("a"), SimpleToken("b")))

        val result = operator.execute("", environment)

        assertEquals("a b\n", result.output)
    }
}