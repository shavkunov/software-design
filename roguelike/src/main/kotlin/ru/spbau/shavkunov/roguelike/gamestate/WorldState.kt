package ru.spbau.shavkunov.roguelike.gamestate

import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.attributes.Inventory
import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.gamestate.interaction.InteractionStrategy
import ru.spbau.shavkunov.roguelike.gamestate.interaction.MonsterStrategy
import ru.spbau.shavkunov.roguelike.gamestate.interaction.PlayerStrategy
import ru.spbau.shavkunov.roguelike.navigation.*
import ru.spbau.shavkunov.roguelike.random

class WorldState {
    val gameMap = GameMap()
    private var player: ObjectWithPosition<ActiveCharacter>
    private val monsters: MutableMap<Position, ActiveCharacter> = mutableMapOf()

    init {
        val playerTile = gameMap.getRandomFreeTile()
        player = ObjectWithPosition(ActiveCharacter(TileType.Player), playerTile.pos)
        gameMap.setTile(MapEntity(TileType.Player, player.pos))
    }

    fun isPlayerDead(): Boolean {
        return player.underlyingObject.isDead()
    }

    fun getPlayerAttributes(): Attributes {
        return player.underlyingObject.currentAttributes
    }

    fun getPlayerInventory(): Inventory {
        return player.underlyingObject.inventory
    }

    private fun update(newPositionTile: TileType, strategy: InteractionStrategy) {
        when(newPositionTile) {
            TileType.Floor -> strategy.proceedFloor(gameMap)
            TileType.Player -> strategy.proceedPlayer(player)
            TileType.Monster -> strategy.proceedMonster(monsters)
            TileType.Lootbox -> strategy.proceedLootboox()

            else -> {}
        }
    }

    fun playerUpdate(playerMove: Move) {
        val newPosition = playerMove.makeMove(player.pos)
        val newPositionTile = gameMap.getTile(newPosition)
        val playerStrategy = PlayerStrategy(player, newPosition)

        update(newPositionTile, playerStrategy)

        moveMonsters()
    }

    private fun moveMonsters() {
        val directions = listOf(Up, Down, Left, Right)

        for (monsterPosition: Position in monsters.keys) {
            val monster = ObjectWithPosition(monsters[monsterPosition]!!, monsterPosition)
            val monsterMove = directions.get(random.nextInt(directions.size))
            val newPosition = monsterMove.makeMove(monsterPosition)
            val newPositionTile = gameMap.getTile(newPosition)
            val monsterStrategy = MonsterStrategy(monsters, monster, newPosition)

            update(newPositionTile, monsterStrategy)
        }
    }
}