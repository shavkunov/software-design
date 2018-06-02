package ru.spbau.shavkunov.roguelike.navigation

interface Move {
    fun makeMove(from: Position): Position
}

object Right : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x, from.y + 1)
    }
}

object Left : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x, from.y - 1)
    }
}

object Up : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x - 1, from.y)
    }
}

object Down : Move {
    override fun makeMove(from: Position): Position {
        return Position(from.x + 1, from.y)
    }
}