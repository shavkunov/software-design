package ru.spbau.shavkunov.interpreter

/**
 * Object contains common operation for escape char string
 */
object StringCommon {

    /**
     * create list of separated words by predicate
     */
    fun split(input: List<EscapeChar>, predicate: (EscapeChar) -> Boolean): List<List<EscapeChar>> {
        val result = mutableListOf<List<EscapeChar>>()
        var nextCommand = mutableListOf<EscapeChar>()

        for (char: EscapeChar in input) {
            if (!predicate(char)) {
                nextCommand.add(char)
            } else {
                result.add(nextCommand)
                nextCommand = mutableListOf()
            }
        }

        if (!nextCommand.isEmpty()) {
            result.add(nextCommand)
        }

        return result
    }
}