package ru.spbau.shavkunov.roguelike.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class MoveTest {

    @Test
    fun movesTest() {
        val initPosition = Position(0, 0)

        assertEquals(Position(-1, 0), Up.makeMove(initPosition))
        assertEquals(Position(1, 0), Down.makeMove(initPosition))
        assertEquals(Position(0, 1), Right.makeMove(initPosition))
        assertEquals(Position(0, -1), Left.makeMove(initPosition))
    }
}