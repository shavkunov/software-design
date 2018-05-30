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
                workingDirectory.setPath(workingDirectory.getPath().resolve(Paths.get(args[0])))

                ""
            }

            else -> "Invalid number of arguments"
        },
        false)
    }
}
