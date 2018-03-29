package kotlin.ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.interpreter.EscapeChar

object StringCommon {
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