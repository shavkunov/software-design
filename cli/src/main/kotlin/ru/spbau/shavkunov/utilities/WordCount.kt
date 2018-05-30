package ru.spbau.shavkunov.utilities

import org.apache.commons.io.FileUtils
import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory
import java.nio.file.Path
import java.nio.file.Paths

/**
 * wc bash command implementation
 * counts lines, words and bytes in file
 */
object WordCount : Utility {
    override fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult {
        val output = if (input.isNotEmpty()) {
            countFromString(input)
        } else {
            countFromArgs(workingDirectory, args)
        }

        return ExecutionResult(output, false)
    }

    /**
     * Reading each file and executing word count on it
     */
    private fun countFromArgs(workingDirectory: WorkingDirectory, args: List<String>): String {
        return args.joinToString("\n") {
            countFromFile(workingDirectory.path.resolve(Paths.get(it)))
        }
    }

    /**
     * Word count from content of file
     */
    private fun countFromFile(filePath: Path) : String {
        val file = filePath.toFile()
        val content = String(FileUtils.readFileToByteArray(file))

        return countFromString(content)
    }

    /**
     * Word count from string content
     */
    private fun countFromString(input: String): String {
        val lines = input.split("\n")
        val bytes = input.toByteArray().size

        val words = lines
                .filter { it != "" }
                .sumBy { it.split(" ").size }

        val linesCount = input.count({ it == '\n' })
        return "$linesCount $words $bytes"
    }
}