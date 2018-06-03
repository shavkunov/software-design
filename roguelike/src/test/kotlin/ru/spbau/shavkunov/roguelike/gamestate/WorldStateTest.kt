package ru.spbau.shavkunov.roguelike.gamestate

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spbau.shavkunov.roguelike.navigation.*

class WorldStateTest {
    val moves = listOf(Up, Down, Left, Right)

    @Test
    fun moveTest() {
        for (move in moves) {
            val worldState = WorldState()
            val initPosition = worldState.getPlayerPosition()
            val prevHealth = worldState.getPlayerAttributes().health

            val newPosition = move.makeMove(initPosition)
            val tile = worldState.gameMap.getTile(newPosition)

            worldState.playerUpdate(move)

            when (tile) {
                TileType.Floor -> {
                    assertEquals(newPosition, worldState.getPlayerPosition())
                }

                TileType.Lootbox -> {
                    assertEquals(newPosition, worldState.getPlayerPosition())
                    assertEquals(1, worldState.getPlayerInventory().unusedItems.size)
                }

                TileType.Wall -> {
                    assertEquals(initPosition, worldState.getPlayerPosition())
                }

                TileType.Monster -> {
                    assertTrue(prevHealth >= worldState.getPlayerAttributes().health)
                }

                else -> {
                }
            }
        }
    }
}