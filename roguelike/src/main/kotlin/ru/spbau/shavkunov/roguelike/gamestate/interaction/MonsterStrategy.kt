package ru.spbau.shavkunov.roguelike.gamestate.interaction

import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.characters.CombatResolver
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.navigation.Position

class MonsterStrategy(
        val monsters: MutableMap<Position, ActiveCharacter>,
        val monster: ObjectWithPosition<ActiveCharacter>,
        val newPosition: Position
) : InteractionStrategy {
    override fun proceedFloor() {
        monsters.remove(monster.pos)
        monster.pos = newPosition
        monsters[newPosition] = monster.underlyingObject
    }

    override fun proceedMonster(monsters: MutableMap<Position, ActiveCharacter>) {

    }

    override fun proceedPlayer(player: ObjectWithPosition<ActiveCharacter>) {
        val pair = Pair(player.underlyingObject, monster.underlyingObject)
        val resolvedPair = CombatResolver.resolveCombat(pair)

        val damagedPlayer = resolvedPair.first
        val damagedMonster = resolvedPair.second

        if (damagedPlayer.isDead()) {
            return
        }

        player.underlyingObject = damagedPlayer
        monsters[monster.pos] = damagedMonster

        if (damagedMonster.isDead()) {
            monsters.remove(monster.pos)
        }
    }

    override fun proceedLootboox() {

    }
}