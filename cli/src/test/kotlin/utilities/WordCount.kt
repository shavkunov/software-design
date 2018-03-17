package utilities

import org.apache.commons.io.FileUtils
import org.junit.Test
import ru.spbau.shavkunov.utilities.WordCount
import java.nio.charset.Charset
import kotlin.test.assertEquals

class WordCountTest {
    @Test
    fun testFromFile() {
        val folder = createTempDir("test")

        val first = folder.resolve("1")
        val second = folder.resolve("2")

        first.createNewFile()
        second.createNewFile()

        val content1 = "123 123\nololo\n123"
        val content2 = "ololo\n"
        FileUtils.writeStringToFile(first, content1, Charset.defaultCharset())
        FileUtils.writeStringToFile(second, content2, Charset.defaultCharset())

        val result = WordCount.execute(listOf(first.absolutePath, second.absolutePath), "")

        assertEquals("2 4 17\n1 1 6", result.output)
        assertEquals(false, result.isExit)
    }

    @Test
    fun testFromInput() {
        val content = "123 123\nololo\n123"
        val result = WordCount.execute(listOf(), content)

        assertEquals("2 4 17", result.output)
        assertEquals(false, result.isExit)
    }
}