package ru.spbau.shavkunov.interpreter

import ru.spbau.shavkunov.Environment

/**
 * Token representing a chunk of input text
 */
interface Token {
    /**
     * Every token can be represented as string
     * Environment used to expand Replace tokens
     */
    fun getStringRepresentation(environment: Environment) : String
}

/**
 * Token that representing some string(in most cases it's just a word)
 */
data class SimpleToken(private val name: String): Token {
    override fun getStringRepresentation(environment: Environment): String {
        return name
    }
}

/**
 * This token also representing string, but it will expend in different value(substitution with $ sign in bash)
 */
data class ReplaceToken(private val name: String): Token {
    override fun getStringRepresentation(environment: Environment): String {
        return environment.getVariable(name)
    }
}

/**
 * Token that stores a few tokens.
 * For example, in one word there is a part, that we need to expand, but only this part,
 * so complex token will store replace and simple tokens
 */
data class ComplexToken(private val tokens: List<Token>): Token {
    override fun getStringRepresentation(environment: Environment): String {
        val sb = StringBuilder()

        for (token: Token in tokens) {
            sb.append(token.getStringRepresentation(environment))
        }

        return sb.toString()
    }
}