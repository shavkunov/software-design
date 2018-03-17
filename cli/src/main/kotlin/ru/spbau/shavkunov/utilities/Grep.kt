package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import ru.spbau.shavkunov.InvalidArgumentException
import java.nio.file.Paths
import java.util.regex.Pattern

object Grep: Utility {
    private val parser: CommandLineParser = DefaultParser()
    private val options: Options = Options()

    init {
        initOptions()
    }

    override fun execute(args: List<String>, input: String): ExecutionResult {
        val commandLine = parser.parse(options, args.toTypedArray())

        val insensitivity = commandLine.hasOption("i")
        val wholeWords = commandLine.hasOption("w")
        var additionalLines = 0
        if (commandLine.hasOption("A")) {
            val text = commandLine.getOptionValue("A")

            additionalLines = Integer.valueOf(text)
        }

        if (commandLine.args.isEmpty()) {
            throw InvalidArgumentException()
        }

        if (input.isNotEmpty()) {
            val pattern = commandLine.args[0]
            val output = grep(input, pattern, insensitivity, wholeWords, additionalLines)

            return ExecutionResult(output, false)
        }

        if (commandLine.args.size < 2) {
            throw InvalidArgumentException()
        }

        val files = commandLine.args.drop(1)
        val pattern = commandLine.args[0]
        val output = files.joinToString("\n", "", "") {
            grep(getFileContent(Paths.get(it)), pattern, insensitivity, wholeWords, additionalLines)
        }

        return ExecutionResult(output, false)
    }

    private fun grep(inputContent: String, inputPattern: String, insensitivity: Boolean,
                     wholeWords: Boolean, additionalLines: Int): String {
        var content = inputContent
        var regex = inputPattern

        if (insensitivity) {
            content = content.toLowerCase()
            regex = regex.toLowerCase()
        }

        if (wholeWords) {
            regex = "\\b$regex\\b"
        }

        val output = mutableListOf<String>()

        val pattern = Pattern.compile(regex)
        val lines = content.split("\n")
        val inputLines = inputContent.split("\n")
        for ((index, line) in lines.withIndex()) {
            val matcher = pattern.matcher(line)

            if (matcher.find()) {
               output.add(addLines(inputLines, index, additionalLines))
            }
        }

        return output.joinToString("\n", "", "\n")
    }

    private fun addLines(lines: List<String>, startIndex: Int, amount: Int): String {
        val slice = lines.drop(startIndex).take(amount + 1)
        return slice.joinToString("\n", "")
    }

    private fun initOptions() {
        val insensitivity = Option("i", "insensitivity mode")
        val word = Option("w", "search whole words")
        val additionalLines = Option("A", "additional lines after found line will be displayed")
        additionalLines.args = 1

        options.addOption(insensitivity)
        options.addOption(word)
        options.addOption(additionalLines)
    }
}