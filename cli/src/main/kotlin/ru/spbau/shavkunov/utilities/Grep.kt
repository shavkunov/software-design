package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import ru.spbau.shavkunov.InvalidArgumentException
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * Bash grep utility implementation
 */
object Grep: Utility {
    private val parser: CommandLineParser = DefaultParser()
    private val options: Options = Options()

    init {
        initOptions()
    }

    /**
     * Executing with following arguments
     * if input stream isn't empty, so there is must be first argument -- pattern to find in input stream
     * if input stream is empty, first argument must be also pattern to match,
     * next arguments -- list of files, content of each will be searched to match pattern
     * pattern can be regex too
     *
     * additional options:
     * -i -- case insensitive search
     * -w -- whole words search
     * -A n -- add to output n additional lines after found line
     */
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

    /**
     * Implementation grep search
     * inputContent -- where to search
     * inputPattern -- what we need to search
     * insensitivity flag -- insensitive search if true
     * same for wholeWords
     * additional lines -- additional lines after found correct line
     */
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

    /**
     * Adding lines starting from startIndex in lines input.
     * Amount of lines specified in amount.
     */
    private fun addLines(lines: List<String>, startIndex: Int, amount: Int): String {
        val slice = lines.drop(startIndex).take(amount + 1)
        return slice.joinToString("\n", "")
    }

    /**
     * Initializing options with extra flags
     */
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