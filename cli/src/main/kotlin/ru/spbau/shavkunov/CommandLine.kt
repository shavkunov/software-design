package ru.spbau.shavkunov

import ru.spbau.shavkunov.interpreter.InputHandler

/**
 * Class that runs CLI and handling user input
 */
class CommandLine(private val environment: Environment) {
    /**
     * Runs REPL
     */
    fun run() {
        while (true) {
            val input = readLine() ?: break

            try {
                val operator = InputHandler.handle(input)
                val executionResult = operator.execute("", environment)
                print(executionResult.output)

                if (executionResult.isExit) {
                    break
                }

               // kotlin doesn't support multiple exceptions:
               // https://discuss.kotlinlang.org/t/does-kotlin-have-multi-catch/486/5
            } catch (parsingException: ParsingException) {
                parsingException.printStackTrace()
            } catch (argumentException: InvalidArgumentException) {
                argumentException.printStackTrace()
            } catch (emptyCommandException: EmptyCommandException) {
                emptyCommandException.printStackTrace()
            }
        }
    }
}