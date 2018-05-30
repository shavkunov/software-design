package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory

/**
 * pwd bash command implementation
 * prints current directory
 */
object Pwd : Utility {
    override fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult {
        return ExecutionResult(workingDirectory.getPath().toString() + "\n", false)
    }
}