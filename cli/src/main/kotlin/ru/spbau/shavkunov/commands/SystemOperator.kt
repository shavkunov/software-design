package ru.spbau.shavkunov.commands

import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.ExecutionResult

/**
 * Operator, which provides system features to user
 */
interface SystemOperator {
    fun execute(input: String, environment: Environment): ExecutionResult
}