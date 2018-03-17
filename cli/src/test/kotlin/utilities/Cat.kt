package utilities

import org.apache.commons.io.FileUtils
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.shavkunov.utilities.Cat
import java.nio.charset.Charset
import kotlin.test.assertEquals

class CatTest {
    @Test
    fun testCat() {
        val folder = createTempDir("test")

        val first = folder.resolve("1")
        val second = folder.resolve("2")

        first.createNewFile()
        second.createNewFile()

        val content1 = "123"
        val content2 = "oollo"

        FileUtils.writeStringToFile(first, content1, Charset.defaultCharset())
        FileUtils.writeStringToFile(second, content2, Charset.defaultCharset())

        val result = Cat.execute(listOf(first.absolutePath, second.absolutePath), "")

        val expected = "123\noollo\n"

        assertEquals(expected, result.output)
        assertEquals(false, result.isExit)
    }
}