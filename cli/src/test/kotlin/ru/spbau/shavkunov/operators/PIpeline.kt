package ru.spbau.shavkunov.operators

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.commands.BuiltinOperator
import ru.spbau.shavkunov.commands.PipelineOperator
import ru.spbau.shavkunov.interpreter.SimpleToken
import ru.spbau.shavkunov.utilities.Cat
import ru.spbau.shavkunov.utilities.Echo
import kotlin.test.assertEquals

class PipelineTest {
    @Test
    fun test() {
        val environment = Environment()
        environment.addUtility("echo", Echo)
        environment.addUtility("cat", Cat)

        val left = BuiltinOperator(SimpleToken("echo"),
                listOf(SimpleToken("a"), SimpleToken("b")))

        val right = BuiltinOperator(SimpleToken("cat"), emptyList())

        val operator = PipelineOperator(left, right)
        val result = operator.execute("", environment)

        assertEquals("a b\n", result.output)
    }
}