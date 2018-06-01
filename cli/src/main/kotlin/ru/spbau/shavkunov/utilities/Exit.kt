package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult

/**
 * Command used to exit from CLI
 */
object Exit : Utility {
    override fun execute(args: List<String>, input: String): ExecutionResult {
        return ExecutionResult("", true)
    }

}