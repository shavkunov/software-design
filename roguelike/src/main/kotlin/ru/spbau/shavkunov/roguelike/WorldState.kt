package ru.spbau.shavkunov.roguelike

import ru.spbau.shavkunov.roguelike.attributes.generateItem
import ru.spbau.shavkunov.roguelike.navigation.Move
import ru.spbau.shavkunov.roguelike.navigation.Position

val monstersRange = 5..10

class WorldState {
    private val gameMap = GameMap()
    private var player: ObjectWithPosition<Character>
    private val monsters: MutableMap<Position, Character> = mutableMapOf()

    init {
        val playerTile = gameMap.getRandomFreeTile()
        player = ObjectWithPosition(Player(), playerTile.pos)
        gameMap.setTile(MapEntity(EntityType.Player, player.pos))

        for (i in 0..random.nextInt(monstersRange)) {
            val monsterTile = gameMap.getRandomFreeTile()
            gameMap.setTile(MapEntity(EntityType.Monster, monsterTile.pos))

            monsters[monsterTile.pos] = Monster()
        }
    }

    fun worldChange(playerMove: Move) {
        val newPosition = playerMove.makeMove(player.pos)
        val newPositionTile = gameMap.getTile(newPosition)

        when(newPositionTile) {
            EntityType.Floor -> {
                gameMap.moveCharacter(player, newPosition)
                player.pos = newPosition
            }

            EntityType.Monster -> {
                val monster = monsters[newPosition]!!
                val pair = Pair(player.underlyingObject, monster)
                val resolvedPair = CombatResolver.resolveCombat(pair)

                val damagedPlayer = resolvedPair.first
                val damagedMonster = resolvedPair.second

                if (damagedPlayer.isDead()) {
                    return
                }

                player = ObjectWithPosition(damagedPlayer, player.pos)
                monsters[newPosition] = damagedMonster

                if (damagedMonster.isDead()) {
                    monsters.remove(newPosition)
                }
            }

            EntityType.Lootbox -> {
                val item = generateItem()
                player.underlyingObject.pickItem(item)
            }

            else -> {

            }
        }
    }
}