package ru.spbau.shavkunov.roguelike.navigation

/**
 * Making move from the current position
 */
interface Move {
    fun makeMove(from: Position): Position
}

/**
 * gets right position from the current
 */
object Right : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x, from.y + 1)
    }
}

/**
 * gets left position from the current
 */
object Left : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x, from.y - 1)
    }
}

/**
 * gets up position from the current
 */
object Up : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x - 1, from.y)
    }
}

/**
 * gets down position from the current
 */
object Down : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x + 1, from.y)
    }
}