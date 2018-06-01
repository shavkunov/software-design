package ru.spbau.shavkunov.utilities

import org.apache.commons.io.FileUtils
import ru.spbau.shavkunov.ExecutionResult
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Cat bash command implementation.
 * Prints input stream, if args are empty, otherwise
 * it will print content of files in args
 */
object Cat : Utility {
    override fun execute(args: List<String>, input: String): ExecutionResult {
        val fileOutput = args.joinToString("\n", "", "\n") { getFileContent(Paths.get(it)) }
        val output = if (args.isEmpty()) input else fileOutput

        return ExecutionResult(output, false)
    }
}