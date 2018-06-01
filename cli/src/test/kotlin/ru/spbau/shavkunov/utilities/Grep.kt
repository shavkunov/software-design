package ru.spbau.shavkunov.utilities

import org.apache.commons.io.FileUtils
import org.junit.Test
import ru.spbau.shavkunov.InvalidArgumentException
import ru.spbau.shavkunov.WorkingDirectory
import ru.spbau.shavkunov.utilities.Grep
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.test.assertEquals

class GrepTest {
    val workingDirectory = WorkingDirectory(Paths.get(System.getProperty("user.dir")))

    @Test
    fun simpleTestFromFile() {
        val file = createTempFile()

        val content = "123 123\nololo\n123"
        FileUtils.writeStringToFile(file, content, Charset.defaultCharset())

        val result = Grep.execute(workingDirectory, listOf("123", file.path), "")
        assertEquals("123 123\n123\n", result.output)
    }

    @Test
    fun simpleTestFromInput() {
        val content = "123 123\nololo\n123"
        val result = Grep.execute(workingDirectory, listOf("123"), content)

        assertEquals("123 123\n123\n", result.output)
    }

    @Test
    fun testCaseInsensitive() {
        val file = createTempFile()

        val content = "test\nololo\nLOL\nword?"
        FileUtils.writeStringToFile(file, content, Charset.defaultCharset())

        val result = Grep.execute(workingDirectory, listOf("lol", file.path, "-i"), "")
        assertEquals("ololo\nLOL\n", result.output)

        val result1 = Grep.execute(workingDirectory, listOf("lol", "-i", file.path), "")
        assertEquals("ololo\nLOL\n", result1.output)

        val result2 = Grep.execute(workingDirectory, listOf("-i", "lol", file.path), "")
        assertEquals("ololo\nLOL\n", result2.output)
    }

    @Test
    fun testWholeWords() {
        val content = "perfect match\nnotperfectmatch\nperfect?\nhello"
        val result = Grep.execute(workingDirectory, listOf("perfect", "-w"), content)

        assertEquals("perfect match\nperfect?\n", result.output)
    }

    @Test(expected = InvalidArgumentException::class)
    fun testAdditionalLines() {
        val content = "123\ntest\ntest\nmaybe another?\ntest\n123"
        val result = Grep.execute(workingDirectory, listOf("123", "-A", "1"), content)
        assertEquals("123\ntest\n123\n", result.output)

        val result1 = Grep.execute(workingDirectory, listOf("123", "-A", "2"), content)
        assertEquals("123\ntest\ntest\n123\n", result1.output)

        val result2 = Grep.execute(workingDirectory, listOf("-A", "3", "123"), content)
        assertEquals("123\ntest\ntest\nmaybe another?\n123\n", result2.output)

        Grep.execute(workingDirectory, listOf("-A", "TEST", "123"), content)
    }

    @Test
    fun testAllArgs() {
        val content = "perfect match\nPERFECT match?\nnotperfectmatch\nperfect?\nhello PeRfEct!"
        val result = Grep.execute(workingDirectory, listOf("perfect", "-w", "-i"), content)

        assertEquals("perfect match\nPERFECT match?\nperfect?\nhello PeRfEct!\n", result.output)
    }

    @Test
    fun testRegex() {
        val content = "#a3c113\n#4d82h4\nis that hex?\n123456"
        val pattern = "^#?([a-f0-9]{6}|[a-f0-9]{3})$"

        val result = Grep.execute(workingDirectory, listOf(pattern), content)
        assertEquals("#a3c113\n123456\n", result.output)
    }
}