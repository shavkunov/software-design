package ru.spbau.shavkunov.roguelike.gamestate.interaction

import ru.spbau.shavkunov.roguelike.attributes.generateItem
import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.characters.CombatResolver
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.navigation.Position

class PlayerStrategy(private val player: ObjectWithPosition<ActiveCharacter>,
                     private val newPosition: Position
) : InteractionStrategy {

    override fun proceedFloor() {
        player.pos = newPosition
    }

    override fun proceedMonster(monsters: MutableMap<Position, ActiveCharacter>) {
        val monster = monsters[newPosition]!!
        val pair = Pair(player.underlyingObject, monster)
        val resolvedPair = CombatResolver.resolveCombat(pair)

        val damagedPlayer = resolvedPair.first
        val damagedMonster = resolvedPair.second

        if (damagedPlayer.isDead()) {
            return
        }

        player.underlyingObject = damagedPlayer
        monsters[newPosition] = damagedMonster

        if (damagedMonster.isDead()) {
            monsters.remove(newPosition)
        }
    }

    override fun proceedPlayer(player: ObjectWithPosition<ActiveCharacter>) {

    }

    override fun proceedLootboox() {
        val item = generateItem()
        player.underlyingObject.pickItem(item)
        player.pos = newPosition
    }
}