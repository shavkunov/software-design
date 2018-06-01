package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult

/**
 * Echo bash command implementation.
 * Prints input stream
 */
object Echo : Utility {
    override fun execute(args: List<String>, input: String): ExecutionResult {
        return ExecutionResult(
                args.joinToString(" ", "", "\n"),
                false
        )
    }

}