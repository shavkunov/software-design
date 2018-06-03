package ru.spbau.shavkunov.utilities

import org.apache.commons.io.FileUtils
import org.junit.Test
import ru.spbau.shavkunov.WorkingDirectory
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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

        val result = Cat.execute(
            WorkingDirectory(Paths.get(System.getProperty("user.dir"))),
            listOf(first.absolutePath, second.absolutePath),
            ""
        )

        val expected = "123\noollo\n"

        assertEquals(expected, result.output)
        assertFalse(result.isExit)
    }
}