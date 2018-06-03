package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory

/**
 * Interface for embedded utility, that our CLI can execute
 */
interface Utility {
    /**
     * Executing command with input stream and command line arguments
     */
    fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult
}