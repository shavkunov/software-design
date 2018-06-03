package ru.spbau.shavkunov.roguelike.gamestate

import org.junit.Assert.assertEquals
import org.junit.Test

class GameMapTest {

    @Test
    fun clearTilesTest() {
        val gameMap = GameMap()
        val monsterTile = gameMap.getRandomFreeTile()
        gameMap.setTile(MapEntity(TileType.Monster, monsterTile.pos))

        val playerTile = gameMap.getRandomFreeTile()
        gameMap.setTile(MapEntity(TileType.Player, playerTile.pos))

        val lootboxTile = gameMap.getRandomFreeTile()
        gameMap.setTile(MapEntity(TileType.Lootbox, lootboxTile.pos))

        gameMap.clearElements()

        assertEquals(TileType.Floor, gameMap.getTile(monsterTile.pos))
        assertEquals(TileType.Floor, gameMap.getTile(playerTile.pos))
        assertEquals(TileType.Lootbox, gameMap.getTile(lootboxTile.pos))
    }

    @Test
    fun managingTiles() {
        val gameMap = GameMap()
        val tile = gameMap.getRandomFreeTile()
        val type = TileType.Player

        gameMap.setTile(MapEntity(type, tile.pos))
        assertEquals(type, gameMap.getTile(tile.pos))
    }
}