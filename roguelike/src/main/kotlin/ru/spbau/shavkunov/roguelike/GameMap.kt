package ru.spbau.shavkunov.roguelike

import ru.spbau.shavkunov.roguelike.navigation.Position
import java.io.File
import java.nio.file.Paths

val resources = Paths.get("src/main/resources")
val defaultMap = "DefaultMap.txt"
val defaultMapFile = resources.resolve(defaultMap).toFile()

class GameMap(mapInput: File = defaultMapFile) {
    private val gameMap: Array<Array<EntityType>>

    init {
        val rows = mapInput.readLines().size
        val columns = mapInput.readLines()[0].length
        gameMap = Array(rows, {Array(columns, {EntityType.Floor})})

        for ((x, line: String) in mapInput.readLines().withIndex()) {
            for ((y, tile: Char) in line.withIndex()) {
                var entity: EntityType
                when(tile) {
                    '.' -> entity = EntityType.Floor
                    else -> entity = EntityType.Wall
                }

                gameMap[x][y] = entity
            }
        }
    }

    fun getRandomFreeTile(): MapEntity {
        val freeTiles = mutableListOf<MapEntity>()
        for ((rowIndex, row) in gameMap.withIndex()) {
            for ((columnIndex, column ) in row.withIndex()) {
                if (column == EntityType.Floor) {
                    freeTiles.add(MapEntity(column, Position(rowIndex, columnIndex)))
                }
            }
        }

        val index = random.nextInt(freeTiles.size)
        return freeTiles[index]
    }

    fun moveCharacter(character: ObjectWithPosition<out Character>, to: Position) {
        val type: EntityType
        if (character.underlyingObject is Player) {
            type = EntityType.Player
        } else {
            type = EntityType.Monster
        }

        setTile(MapEntity(EntityType.Floor, character.pos))
        setTile(MapEntity(type, to))
    }

    fun getTile(pos: Position): EntityType {
        return gameMap[pos.x][pos.y]
    }

    fun setTile(entity: MapEntity) {
        gameMap[entity.pos.x][entity.pos.y] = entity.underlyingObject
    }
}

data class ObjectWithPosition<T>(val underlyingObject: T, var pos: Position)

typealias MapEntity = ObjectWithPosition<EntityType>

enum class EntityType {
    Floor, Lootbox, Wall, Player, Monster
}