package utilities

import org.junit.Test
import ru.spbau.shavkunov.WorkingDirectory
import ru.spbau.shavkunov.utilities.Echo
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class EchoTest {
    @Test
    fun test() {
        val result = Echo.execute(
            WorkingDirectory(Paths.get(System.getProperty("user.dir"))),
            listOf("123"), ""
        )

        assertEquals("123\n", result.output)
        assertFalse(result.isExit)
    }
}