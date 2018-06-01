package ru.spbau.shavkunov


/**
 * Result of each command execution
 */
data class ExecutionResult(val output: String, val isExit: Boolean)