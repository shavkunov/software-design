package ru.spbau.shavkunov.commands

import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.ExecutionResult

data class PipelineOperator(val leftCommand: SystemOperator, val rightCommand: SystemOperator) : SystemOperator {
    override fun execute(input: String, environment: Environment): ExecutionResult {
        val firstResult = leftCommand.execute(input, environment)

        if (firstResult.isExit) {
            return firstResult
        }

        return rightCommand.execute(firstResult.output, environment)
    }
}