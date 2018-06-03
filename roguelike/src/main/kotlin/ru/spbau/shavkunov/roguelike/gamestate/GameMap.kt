package ru.spbau.shavkunov.roguelike.gamestate

import ru.spbau.shavkunov.roguelike.navigation.Position
import ru.spbau.shavkunov.roguelike.random
import java.io.File
import java.nio.file.Paths

val resources = Paths.get("src/main/resources")
val defaultMap = "DefaultMap.txt"
val defaultMapFile = resources.resolve(defaultMap).toFile()

class GameMap(mapInput: File = defaultMapFile) {
    private val gameMap: Array<Array<TileType>>
    val rows: Int = mapInput.readLines().size
    val columns: Int = mapInput.readLines()[0].length

    init {
        gameMap = Array(rows, {Array(columns, { TileType.Floor })})

        for ((x, line: String) in mapInput.readLines().withIndex()) {
            for ((y, tile: Char) in line.withIndex()) {
                var entity: TileType
                when(tile) {
                    '.' -> entity = TileType.Floor
                    else -> entity = TileType.Wall
                }

                gameMap[x][y] = entity
            }
        }
    }

    fun clearElements() {
        for ((rowIndex, row) in gameMap.withIndex()) {
            for ((columnIndex, column ) in row.withIndex()) {
                if (column == TileType.Player || column == TileType.Monster) {
                    setTile(MapEntity(TileType.Floor, Position(rowIndex, columnIndex)))
                }
            }
        }
    }

    fun getRandomFreeTile(): MapEntity {
        val freeTiles = mutableListOf<MapEntity>()
        for ((rowIndex, row) in gameMap.withIndex()) {
            for ((columnIndex, column ) in row.withIndex()) {
                if (column == TileType.Floor) {
                    freeTiles.add(MapEntity(column, Position(rowIndex, columnIndex)))
                }
            }
        }

        val index = random.nextInt(freeTiles.size)
        return freeTiles[index]
    }

    fun getTile(pos: Position): TileType {
        return gameMap[pos.x][pos.y]
    }

    fun setTile(entity: MapEntity) {
        gameMap[entity.pos.x][entity.pos.y] = entity.underlyingObject
    }
}

data class ObjectWithPosition<T>(var underlyingObject: T, var pos: Position)

typealias MapEntity = ObjectWithPosition<TileType>

enum class TileType(val value: Char) {
    Floor('.'), Lootbox('X'), Wall('#'), Player('@'), Monster('M');
}