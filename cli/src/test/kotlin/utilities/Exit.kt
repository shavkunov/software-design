package utilities

import org.junit.Test
import ru.spbau.shavkunov.WorkingDirectory
import ru.spbau.shavkunov.utilities.Exit
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExitTest {
    @Test
    fun test() {
        val workingDirectory = WorkingDirectory(Paths.get(System.getProperty("user.dir")))

        val result1 = Exit.execute(workingDirectory, emptyList(), "")
        val result2 = Exit.execute(workingDirectory, listOf("123", "1"), "")
        val result3 = Exit.execute(workingDirectory, emptyList(), "123")

        assertTrue(result1.isExit)
        assertEquals("", result1.output)

        assertTrue(result2.isExit)
        assertEquals("", result2.output)

        assertTrue(result3.isExit)
        assertEquals("", result3.output)
    }
}