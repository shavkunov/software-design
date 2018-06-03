package ru.spbau.shavkunov.utilities

import ru.spbau.shavkunov.ExecutionResult
import ru.spbau.shavkunov.WorkingDirectory
import java.nio.file.Paths

/**
 * This utility changes current working directory.
 */
object ChangeDirectory : Utility {
    override fun execute(
        workingDirectory: WorkingDirectory,
        args: List<String>,
        input: String
    ): ExecutionResult {
        return ExecutionResult(when (args.size) {
            0 -> {
                workingDirectory.setPath(Paths.get(System.getProperty("user.home")))

                ""
            }

            1 -> {
                val newPath = workingDirectory.getPath().resolve(Paths.get(args[0]))
                val newFile = newPath.toFile()

                if (!newFile.exists()) {
                    return ExecutionResult(noSuchFolderMessage, false)
                }

                if (!newFile.isDirectory) {
                    return ExecutionResult(notAFolderMessage, false)
                }

                workingDirectory.setPath(newPath)

                ""
            }

            else -> invalidNumOfArgsMessage
        },
        false)
    }

    const val noSuchFolderMessage = "No such folder"

    const val notAFolderMessage = "Not a folder"

    const val invalidNumOfArgsMessage = "Invalid number of arguments"
}
