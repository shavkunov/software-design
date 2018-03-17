package operators

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.commands.AssignmentOperator
import kotlin.test.assertEquals

class Assignment {
    @Test
    fun test() {
        val environment = Environment()
        val operator = AssignmentOperator("a", "b")
        operator.execute("", environment)

        assertEquals("b", environment.getVariable("a"))
    }
}