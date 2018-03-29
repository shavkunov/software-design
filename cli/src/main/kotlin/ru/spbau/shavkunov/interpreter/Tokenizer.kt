package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.ParsingException
import ru.spbau.shavkunov.interpreter.InputHandler.split
import ru.spbau.shavkunov.interpreter.Preprocessor.Companion.strong
import ru.spbau.shavkunov.interpreter.Preprocessor.Companion.substitution
import ru.spbau.shavkunov.interpreter.Preprocessor.Companion.weak

/**
 * Tokenizer creates list of tokens from user input
 */
object Tokenizer {
    private var droppedSymbols = 0
    private val stringPredicate = { it: EscapeChar ->
        it.character == strong ||
        it.character == weak ||
        it.character == substitution ||
        it.character.isWhitespace()
    }

    private val escapedStringPredicate = { it: EscapeChar ->
        it.character == strong ||
        it.character == weak ||
        it.character == substitution
    }

    /**
     * Creating tokens from user input
     */
    fun tokenize(input: List<EscapeChar>): List<Token> {
        val words = split(input, { !it.isEscaped && it.character.isWhitespace()})
                    .filter { it.isNotEmpty() }
                    .map { processToken(it) }

        return words
    }

    /**
     * Processing each token after splitting by space
     */
    private fun processToken(input: List<EscapeChar>): Token {
        val list = mutableListOf<Token>()

        var input = input
        while (input.isNotEmpty()) {
            var token: Token?

            when (input.first().character) {
                substitution -> {
                    token = processSubstitution(input.drop(1))
                    input = input.drop(droppedSymbols + 1)
                }

                strong -> {
                    token = processStrongQuotes(input.drop(1))
                    input = input.drop(droppedSymbols + 1)
                }

                weak -> {
                    token = processWeakQuotes(input.drop(1))
                    input = input.drop(droppedSymbols + 1)
                }

                else -> {
                    token = processSimpleToken(input, stringPredicate)
                    input = input.drop(droppedSymbols)
                }
            }
            list.add(token)
        }

        return ComplexToken(list)
    }

    /**
     * Process tokens which are strongly escaped
     */
    @Throws(ParsingException::class)
    private fun processStrongQuotes(input: List<EscapeChar>): Token {
        droppedSymbols = 0
        val indexOfStrong = input.indexOfFirst { it.character == strong }

        if (indexOfStrong == -1) {
            throw ParsingException()
        }

        val name = createString(input.take(indexOfStrong))
        droppedSymbols += name.length + 1
        return SimpleToken(name)
    }

    /**
     * Creating simple token with predicate to stop
     */
    private fun processString(input: List<EscapeChar>, predicateToStop: (EscapeChar) -> Boolean): String {
        droppedSymbols = 0
        var indexOfEnd = input.indexOfFirst { predicateToStop(it) }

        if (indexOfEnd == -1) {
            indexOfEnd = input.size
        }

        val name = createString(input.take(indexOfEnd))
        droppedSymbols += name.length
        return name
    }

    /**
     * Processing simple token
     */
    private fun processSimpleToken(input: List<EscapeChar>, predicate: (EscapeChar) -> Boolean): Token {
        return SimpleToken(processString(input, predicate))
    }

    /**
     * Almost as simple token, but returns Replace token instead
     */
    private fun processSubstitution(input: List<EscapeChar>): Token {
        return ReplaceToken(processString(input, stringPredicate))
    }

    /**
     * Process tokens which are weakly escaped
     */
    @Throws(ParsingException::class)
    private fun processWeakQuotes(input: List<EscapeChar>): Token {
        var indexOfWeak = input.indexOfFirst { it.character == weak }

        if (indexOfWeak == -1) {
            throw ParsingException()
        }

        var input = input
        val list = mutableListOf<Token>()
        var inputSize = indexOfWeak
        while (inputSize != 0) {
            var token: Token? = null
            when (input.first().character) {
                substitution -> {
                    token = processSubstitution(input.drop(1))
                }

                strong -> {
                    token = processStrongQuotes(input.drop(1))
                }
            }

            if (token != null) {
                input = input.drop(1 + droppedSymbols)
                inputSize -= 1 + droppedSymbols
                list.add(token)
                continue
            }

            list.add(processSimpleToken(input, escapedStringPredicate))
            input = input.drop(droppedSymbols)
            inputSize -= droppedSymbols
        }

        droppedSymbols = indexOfWeak + 1

        return ComplexToken(list)
    }
}