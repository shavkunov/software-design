package ru.spbau.shavkunov

import ru.spbau.shavkunov.utilities.*

/**
 * Init default environment and run CLI
 */
fun main(args: Array<String>) {
    val cli = CommandLine(createStandardEnvironment())

    cli.run()
}
