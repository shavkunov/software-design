package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.EmptyCommandException
import ru.spbau.shavkunov.commands.BuiltinOperator
import ru.spbau.shavkunov.commands.SystemOperator

/**
 * Creating simple standard command with first token as a name and other tokens -- as args.
 */
object BuiltinCommandParser : Parser {
    @Throws(EmptyCommandException::class)
    override fun parse(input: List<EscapeChar>): SystemOperator {
        val tokens = Tokenizer.tokenize(input)

        if (tokens.isEmpty()) {
            throw EmptyCommandException()
        }

        return BuiltinOperator(tokens.first(), tokens.drop(1))
    }
}