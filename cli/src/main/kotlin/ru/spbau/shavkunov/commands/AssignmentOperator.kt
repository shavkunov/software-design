package ru.spbau.shavkunov.commands

import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.ExecutionResult

/**
 * Operator assigning to environment variable name with value
 */
data class AssignmentOperator(val name: String, val value: String) : SystemOperator {
    override fun execute(input: String, environment: Environment): ExecutionResult {
        environment.setVariable(name, value)

        return ExecutionResult("", false)
    }
}