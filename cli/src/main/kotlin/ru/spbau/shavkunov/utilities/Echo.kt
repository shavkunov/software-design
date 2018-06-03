package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory

/**
 * Echo bash command implementation.
 * Prints input stream
 */
object Echo : Utility {
    override fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult {
        return ExecutionResult(
                args.joinToString(" ", "", "\n"),
                false
        )
    }

}