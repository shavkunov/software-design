package ru.spbau.shavkunov.roguelike.gamestate.strategy

import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.gamestate.TileType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.gamestate.interaction.InteractionStrategy
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up

class MonsterStrategyTest {
    val moves = listOf(Up, Down, Left, Right)

    @Test
    fun strategyTest() {
        for (move in moves) {
            val mockStrategy = Mockito.mock(InteractionStrategy::class.java)
            val worldState = WorldState()


            val monsterPosition = worldState.monsters.keys.first()
            val newPosition = move.makeMove(monsterPosition)
            val tile = worldState.gameMap.getTile(newPosition)

            worldState.update(tile, mockStrategy)

            when(tile) {
                TileType.Floor -> Mockito.verify(mockStrategy, Mockito.times(1)).proceedFloor()
                TileType.Lootbox -> Mockito.verify(mockStrategy, Mockito.times(1)).proceedLootboox()
                TileType.Monster -> Mockito.verify(mockStrategy, Mockito.times(1)).proceedMonster(Matchers.any())
                TileType.Player -> {
                    val player = ObjectWithPosition(worldState.getPlayer(), worldState.getPlayerPosition())
                    Mockito.verify(mockStrategy, Mockito.times(1)).proceedPlayer(player)
                }
                else -> {}
            }
        }
    }
}