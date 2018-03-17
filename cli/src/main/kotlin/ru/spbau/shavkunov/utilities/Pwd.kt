package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult

/**
 * pwd bash command implementation
 * prints current directory
 */
object Pwd : Utility {
    override fun execute(args: List<String>, input: String): ExecutionResult {
        return ExecutionResult(System.getProperty("user.dir") + "\n", false)
    }
}