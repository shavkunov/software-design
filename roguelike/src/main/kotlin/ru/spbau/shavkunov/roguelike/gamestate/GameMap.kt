package ru.spbau.shavkunov.roguelike.gamestate

import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.navigation.Position
import ru.spbau.shavkunov.roguelike.nextInt
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
    val lootBoxesRange = 5..8

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

        val lootboxesAmount = random.nextInt(lootBoxesRange)
        repeat(lootboxesAmount, {
            val tile = getRandomFreeTile()
            val lootBoxEntity = MapEntity(TileType.Lootbox, tile.pos)

            setTile(lootBoxEntity)
        })
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

    fun moveCharacter(character: ObjectWithPosition<out ActiveCharacter>, to: Position) {
        val type = character.underlyingObject.tileType

        setTile(MapEntity(TileType.Floor, character.pos))
        setTile(MapEntity(type, to))
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
    Floor('.'), Lootbox('X'), Wall('#'), Player('P'), Monster('M');
}