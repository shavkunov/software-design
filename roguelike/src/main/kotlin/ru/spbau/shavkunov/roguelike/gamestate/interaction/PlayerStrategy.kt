package ru.spbau.shavkunov.roguelike.gamestate.interaction

import org.slf4j.LoggerFactory
import ru.spbau.shavkunov.roguelike.attributes.generateItem
import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.characters.CombatResolver
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.navigation.Position

private val LOGGER = LoggerFactory.getLogger("PlayerStrategy")

/**
 * Interacting strategy for the player
 */
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
        player.underlyingObject = damagedPlayer

        if (damagedPlayer.isDead()) {
            return
        }

        monsters[newPosition] = damagedMonster

        if (damagedMonster.isDead()) {
            LOGGER.info("Player killed the monster on the position: {}", newPosition)
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