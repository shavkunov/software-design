package ru.spbau.shavkunov

/**
 * It's thrown, when tokenizer can't tokenize string
 */
class ParsingException : Exception()

/**
 * It's thrown, when grep command have problems with arguments
 */
class InvalidArgumentException: Exception()

/**
 * It's thrown when user provided empty command
 */
class EmptyCommandException : Exception()