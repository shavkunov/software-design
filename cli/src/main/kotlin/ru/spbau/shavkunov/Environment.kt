package ru.spbau.shavkunov

import ru.spbau.shavkunov.utilities.Utility
import java.nio.charset.Charset


/**
 * Environment stores commands and variables inside.
 * Commands is a set of commands that our CLI can execute, otherwise bash command will be executed
 * variables are aliases, created by $ sign
 */
class Environment {
    private val commands: MutableMap<String, Utility> = mutableMapOf()
    private val variables: MutableMap<String, String> = mutableMapOf()

    /**
     * Adding utility to execute
     */
    fun addUtility(name: String, command: Utility) {
        commands[name] = command
    }

    /**
     * Executing utility with specified name
     * args -- arguments from command line
     * input -- is input stream from another commmand
     */
    fun executeCommand(name: String, args: List<String>, input: String): ExecutionResult {
        return commands[name]?.execute(args, input) ?: executeOuterCommand(name, args, input)
    }

    /**
     * Storing value for string name
     */
    fun setVariable(name: String, value: String) {
        variables[name] = value
    }

    /**
     * Getting expanding value for name
     */
    fun getVariable(name: String): String {
        return variables.getOrDefault(name, "")
    }

    /**
     * Executing bash command
     */
    private fun executeOuterCommand(
            name: String,
            args: List<String>,
            input: String
    ): ExecutionResult {
        val process = Runtime.getRuntime().exec(arrayOf(name) + args.toTypedArray())

        process.outputStream.write(input.toByteArray())
        process.waitFor()

        val output = process.inputStream.readBytes().toString(Charset.defaultCharset())
        val isExit = process.exitValue() != 0

        return ExecutionResult(output, isExit)
    }
}



