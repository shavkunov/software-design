package parsers

import org.junit.Test
import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.interpreter.PipelineParser
import ru.spbau.shavkunov.utilities.Echo
import ru.spbau.shavkunov.utilities.WordCount
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PipelineTest {
    @Test
    fun pipelineTest() {
        val text = createEscapedString("echo 123 | wc")

        val env = Environment()
        env.addUtility("echo", Echo)
        env.addUtility("wc", WordCount)

        val operator = PipelineParser.parse(text)!!
        val result = operator.execute("", env)

        assertEquals("1 1 4", result.output)
    }

    @Test
    fun pipelineTestEmptyEnv() {
        val text = createEscapedString("echo 123 | wc")

        val env = Environment()
        env.addUtility("wc", WordCount)
        val operator = PipelineParser.parse(text)!!
        val result = operator.execute("", env)

        assertEquals("1 1 4", result.output)
    }

    @Test
    fun noPipeline() {
        val text = createEscapedString("echo 123")

        val operator = PipelineParser.parse(text)
        assertNull(operator)
    }
}