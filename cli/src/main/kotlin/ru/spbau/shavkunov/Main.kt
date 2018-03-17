package ru.spbau.shavkunov

import ru.spbau.shavkunov.utilities.*

/**
 * Init default environment and run CLI
 */
fun main(args: Array<String>) {
    val environment = Environment()
    environment.addUtility("cat", Cat)
    environment.addUtility("echo", Echo)
    environment.addUtility("exit", Exit)
    environment.addUtility("pwd", Pwd)
    environment.addUtility("wc", WordCount)

    val cli = CommandLine(environment)

    cli.run()
}
