package ru.spbau.shavkunov.commands

import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.ExecutionResult

/**
 * Operator, which takes two operators and give output from executing first operator to second as input stream
 */
data class PipelineOperator(val leftCommand: SystemOperator, val rightCommand: SystemOperator) : SystemOperator {
    override fun execute(input: String, environment: Environment): ExecutionResult {
        val firstResult = leftCommand.execute(input, environment)

        if (firstResult.isExit) {
            return firstResult
        }

        return rightCommand.execute(firstResult.output, environment)
    }
}