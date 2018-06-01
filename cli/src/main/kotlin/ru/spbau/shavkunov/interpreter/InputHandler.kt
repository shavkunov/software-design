package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.commands.SystemOperator

/**
 * Handler creates from user input command to execute
 */
object InputHandler {
    private val preprocessor = Preprocessor()

    fun handle(input: String): SystemOperator {
        val chars = preprocessor.process(input)

        val command = PipelineParser.parse(chars)

        return command ?: parseCommand(chars)
    }

    fun parseCommand(chars: List<EscapeChar>): SystemOperator {
        return AssignmentParser.parse(chars) ?: BuiltinCommandParser.parse(chars)
    }
}