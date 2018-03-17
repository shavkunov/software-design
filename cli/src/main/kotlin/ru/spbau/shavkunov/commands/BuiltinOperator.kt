package ru.spbau.shavkunov.commands

import ru.spbau.shavkunov.Environment
import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.interpreter.Token

class BuiltinOperator(val name: Token, val args: List<Token>) : SystemOperator {
    override fun execute(input: String, environment: Environment): ExecutionResult {
        return environment.executeCommand(
                name.getStringRepresentation(environment),
                args.map { it.getStringRepresentation(environment) },
                input
        )
    }
}