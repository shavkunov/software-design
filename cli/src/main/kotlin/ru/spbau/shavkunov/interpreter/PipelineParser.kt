package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.EmptyCommandException
import ru.spbau.shavkunov.commands.PipelineOperator
import ru.spbau.shavkunov.commands.SystemOperator
import ru.spbau.shavkunov.interpreter.InputHandler.parseCommand
import ru.spbau.shavkunov.interpreter.Preprocessor.Companion.pipe
import ru.spbau.shavkunov.interpreter.StringCommon

/**
 * Pipeline parser creates several commands divided by | and executes them consistently, like bash pipeline
 */
object PipelineParser : Parser {
    @Throws(EmptyCommandException::class)
    override fun parse(input: List<EscapeChar>): SystemOperator? {
        val commands = split(input, pipe)

        if (commands.size == 1) {
            return null
        }

        if (commands.isEmpty()) {
            throw EmptyCommandException()
        }

        while (commands.size > 1) {
            val rightCommand = commands.removeAt(commands.lastIndex)
            val leftCommand = commands.removeAt(commands.lastIndex)

            commands.add(PipelineOperator(leftCommand, rightCommand))
        }

        return commands[0]
    }

    private fun split(input: List<EscapeChar>, delimeter: Char): MutableList<SystemOperator> {
        val pipeSplit = StringCommon.split(input, { it.character == delimeter } )

        return pipeSplit.map { parseCommand(it) }.toMutableList()
    }
}