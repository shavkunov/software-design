package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.commands.SystemOperator

/**
 * Parser creates from user input command, that need to execute
 */
interface Parser {
    fun parse(input: List<EscapeChar>): SystemOperator?
}