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

            else -> return ExecutionResult(invalidNumOfArgsMessage, false)
        }.toFile()

        if (!directory.exists()) {
            return ExecutionResult(noSuchElementMessage, false)
        }

        if (directory.isFile) {
            return ExecutionResult(directory.name + System.lineSeparator(), false)
        }

        return ExecutionResult(
            directory.list().joinToString(System.lineSeparator(), postfix=System.lineSeparator()),
            false
        )
    }

    const val invalidNumOfArgsMessage = "Invalid number of arguments"

    const val noSuchElementMessage = "No such file or directory"
}
