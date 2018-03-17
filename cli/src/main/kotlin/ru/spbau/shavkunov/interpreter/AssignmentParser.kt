package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.commands.AssignmentOperator
import ru.spbau.shavkunov.commands.SystemOperator
import ru.spbau.shavkunov.interpreter.Preprocessor.Companion.assign

/**
 * Assignment parser attempts to find = sign and add variable to environment
 */
object AssignmentParser : Parser {
    override fun parse(input: List<EscapeChar>): SystemOperator? {
        val index = findAssignmentIndex(input) ?: return null

        val left = input.take(index)
        val right = input.drop(index + 1)

        return AssignmentOperator(createString(left), createString(right))
    }

    private fun findAssignmentIndex(input: List<EscapeChar>) : Int? {
        var assignIndex: Int? = null

        for ((index, char: EscapeChar) in input.withIndex()) {
            if (assignIndex != null && char.character == assign) {
                return null
            }

            if (char.character == assign) {
                assignIndex = index
            }

        }

        return assignIndex
    }
}