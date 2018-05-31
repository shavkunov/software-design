package ru.spbau.shavkunov

import ru.spbau.shavkunov.utilities.*
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths


/**
 * Environment stores commands and variables inside.
 * Commands is a set of commands that our CLI can execute, otherwise bash command will be executed
 * variables are aliases, created by $ sign
 */
class Environment {
    private val commands: MutableMap<String, Utility> = mutableMapOf()
    private val variables: MutableMap<String, String> = mutableMapOf()

    private val workingDirectory = WorkingDirectory(Paths.get(System.getProperty("user.dir")))

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
        if (commands[name] != null) {
            return commands[name]!!.execute(workingDirectory, args, input)
        }

        return try {
            executeOuterCommand(name, args, input)
        } catch (io: IOException) {
            ExecutionResult("Command $name failed: ${io.message}", false)
        } catch (interrupted: InterruptedException) {
            ExecutionResult("Command $name failed: ${interrupted.message}", false)
        }
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

/**
 * This class stores path to a current working directory and allows to change it.
 */
class WorkingDirectory(path: Path) {
    private var path: Path = path.toAbsolutePath().normalize()

    fun getPath(): Path {
        return path
    }

    fun setPath(newPath: Path) {
        path = newPath.toAbsolutePath().normalize()
    }
}

fun createStandardEnvironment(): Environment {
    val environment = Environment()
    environment.addUtility("cat", Cat)
    environment.addUtility("echo", Echo)
    environment.addUtility("exit", Exit)
    environment.addUtility("pwd", Pwd)
    environment.addUtility("wc", WordCount)
    environment.addUtility("grep", Grep)
    environment.addUtility("cd", ChangeDirectory)
    environment.addUtility("ls", ListFiles)

    return environment
}
