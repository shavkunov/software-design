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

    fun split(input: List<EscapeChar>, predicate: (EscapeChar) -> Boolean): List<List<EscapeChar>> {
        val result = mutableListOf<List<EscapeChar>>()
        var nextCommand = mutableListOf<EscapeChar>()

        for (char: EscapeChar in input) {
            if (!predicate(char)) {
                nextCommand.add(char)
            } else {
                result.add(nextCommand)
                nextCommand = mutableListOf()
            }
        }

        if (!nextCommand.isEmpty()) {
            result.add(nextCommand)
        }

        return result
    }
}