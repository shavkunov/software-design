package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory
import java.nio.file.Paths

/**
 * This utility prints all files and folders that are located in the given directory.
 */
object ListFiles : Utility {
    override fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult {
        val directory = when (args.size) {
            0 -> workingDirectory.getPath()

            1 -> workingDirectory.getPath().resolve(Paths.get(args[0]))

            else -> return ExecutionResult("Invalid number of arguments", false)
        }.toFile()

        if (!directory.exists()) {
            return ExecutionResult("No such file or directory", false)
        }

        return ExecutionResult(
            directory.list().joinToString(System.lineSeparator(), postfix=System.lineSeparator()),
            false
        )
    }
}
