package ru.spbau.shavkunov.interpreter

/**
 * Initial string processing.
 * Preprocessor identifies escape char sequences.
 */
class Preprocessor {
    companion object {
        const val weak = '"'
        const val strong = '\''
        const val substitution = '$'
        const val assign = '='
        const val pipe = '|'
    }

    private enum class EscapeType {
        WEAK, STRONG, NONE
    }

    private var escaping = EscapeType.NONE

    /**
     * Transform user input string to string with EscapeChars
     * Each EscapeChars contain information is char escaped or not.
     */
    fun process(string: String): List<EscapeChar> {
        escaping = EscapeType.NONE
        val result = mutableListOf<EscapeChar>()

        for (char : Char in string) {
            val isEscaped = checkEscape(char)
            result.add(EscapeChar(char, isEscaped))
        }

        return result
    }

    private fun checkEscape(char: Char): Boolean {
        when (escaping) {
            EscapeType.WEAK -> {
                if (char == weak) {
                    escaping = EscapeType.NONE
                }

                return true
            }

            EscapeType.STRONG -> {
                if (char == strong) {
                    escaping = EscapeType.NONE
                }

                return char != substitution
            }

            EscapeType.NONE -> {
                if (char == strong) {
                    escaping = EscapeType.STRONG
                }

                if (char == weak) {
                    escaping = EscapeType.WEAK
                }

                return false
            }
        }
    }
}

/**
 * Class wrapper for char.
 * Stores addtional information about escaping.
 */
data class EscapeChar(val character: Char, val isEscaped: Boolean)

/**
 * Transform list of chars into standard string
 */
fun createString(chars : List<EscapeChar>): String {
    val sb = StringBuilder()

    for (char: EscapeChar in chars) {
        sb.append(char.character)
    }

    return sb.toString()
}