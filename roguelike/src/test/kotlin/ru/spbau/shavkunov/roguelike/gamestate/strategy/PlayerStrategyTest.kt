package ru.spbau.shavkunov.roguelike.gamestate.strategy

import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.gamestate.TileType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.gamestate.interaction.InteractionStrategy
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up

class PlayerStrategyTest {
    val moves = listOf(Up, Down, Left, Right)

    @Test
    fun strategyTest() {
        for (move in moves) {
            val mockStrategy = mock(InteractionStrategy::class.java)
            val worldState = WorldState()

            val playerPosition = worldState.getPlayerPosition()
            val newPosition = move.makeMove(playerPosition)
            val tile = worldState.gameMap.getTile(newPosition)

            worldState.update(tile, mockStrategy)

            when(tile) {
                TileType.Floor -> verify(mockStrategy, times(1)).proceedFloor()
                TileType.Lootbox -> verify(mockStrategy, times(1)).proceedLootboox()
                TileType.Monster -> verify(mockStrategy, times(1)).proceedMonster(Matchers.any())

                else -> {}
            }

            val player = ObjectWithPosition(worldState.getPlayer(), worldState.getPlayerPosition())
            verify(mockStrategy, times(0)).proceedPlayer(player)
        }
    }
}