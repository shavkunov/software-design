package ru.spbau.shavkunov.parsers

import ru.spbau.shavkunov.interpreter.EscapeChar

fun createEscapedString(text: String = "ololo wifi"): List<EscapeChar> {
    val list = mutableListOf<EscapeChar>()
    for (char: Char in text) {
        list.add(EscapeChar(char, false))
    }

    return list.toList()
}

fun createEscapedStringWithAssign(): List<EscapeChar> {
    return createEscapedString("ololo=wifi")
}