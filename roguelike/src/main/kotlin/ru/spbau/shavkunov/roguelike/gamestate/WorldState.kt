package ru.spbau.shavkunov.roguelike.gamestate

import org.codetome.zircon.api.Size
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.attributes.Inventory
import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.gamestate.interaction.InteractionStrategy
import ru.spbau.shavkunov.roguelike.gamestate.interaction.MonsterStrategy
import ru.spbau.shavkunov.roguelike.gamestate.interaction.PlayerStrategy
import ru.spbau.shavkunov.roguelike.navigation.*
import ru.spbau.shavkunov.roguelike.nextInt
import ru.spbau.shavkunov.roguelike.random

val additionalRows = 10

/**
 * Object represents state of the game. All the model data is stores here.
 */
open class WorldState {
    private var player: ObjectWithPosition<ActiveCharacter>
    val monsters: MutableMap<Position, ActiveCharacter> = mutableMapOf()
    private val lootBoxesRange = 5..8
    private val monstersRange = 10..15
    val gameMap = GameMap()
        get() {
            if (player != null) {

                field.clearElements()
                field.setTile(MapEntity(TileType.Player, player.pos))

                for (monsterPosition: Position in monsters.keys) {
                    field.setTile(MapEntity(TileType.Monster, monsterPosition))
                }
            }

            return field
        }

    init {
        val playerTile = gameMap.getRandomFreeTile()
        player = ObjectWithPosition(ActiveCharacter(TileType.Player), playerTile.pos)
        gameMap.setTile(MapEntity(TileType.Player, player.pos))

        generateItems(TileType.Lootbox, lootBoxesRange)
        val monsterTiles = generateItems(TileType.Monster, monstersRange)

        for (tile in monsterTiles) {
            monsters[tile.pos] = ActiveCharacter(TileType.Monster)
        }
    }

    private fun generateItems(tileType: TileType, randomRange: IntRange): MutableList<MapEntity> {
        val itemsAmount = random.nextInt(randomRange)
        val tiles = mutableListOf<MapEntity>()

        repeat(itemsAmount, {
            val tile = gameMap.getRandomFreeTile()
            val entity = MapEntity(tileType, tile.pos)

            gameMap.setTile(entity)
            tiles.add(entity)
        })

        return tiles
    }

    open fun getMapSize(): Size {
        return Size.of(gameMap.columns, gameMap.rows + additionalRows)
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

    fun getPlayer(): ActiveCharacter {
        return player.underlyingObject
    }

    fun getPlayerPosition(): Position {
        return player.pos
    }

    fun monstersLeft(): Int {
        return monsters.size
    }

    fun update(newPositionTile: TileType, strategy: InteractionStrategy) {
        when(newPositionTile) {
            TileType.Floor -> strategy.proceedFloor()
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
        val monstersKeySet = monsters.keys.toSet()

        for (monsterPosition: Position in monstersKeySet) {
            val monster = ObjectWithPosition(monsters[monsterPosition]!!, monsterPosition)
            val monsterMove = directions.get(random.nextInt(directions.size))
            val newPosition = monsterMove.makeMove(monsterPosition)
            val newPositionTile = gameMap.getTile(newPosition)
            val monsterStrategy = MonsterStrategy(monsters, monster, newPosition)

            update(newPositionTile, monsterStrategy)
        }
    }
}