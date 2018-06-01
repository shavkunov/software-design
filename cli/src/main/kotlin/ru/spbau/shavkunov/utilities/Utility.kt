package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult

/**
 * Interface for embedded utility, that our CLI can execute
 */
interface Utility {
    /**
     * Executing command with input stream and command line arguments
     */
    fun execute(args: List<String>, input: String): ExecutionResult
}