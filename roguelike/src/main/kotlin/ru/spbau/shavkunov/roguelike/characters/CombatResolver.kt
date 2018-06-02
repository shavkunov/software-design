package ru.spbau.shavkunov.roguelike.characters

import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.TileType
import ru.spbau.shavkunov.roguelike.isDexterityHappened
import ru.spbau.shavkunov.roguelike.isLuckHappend

object CombatResolver {
    fun resolveCombat(pair: Pair<ActiveCharacter, ActiveCharacter>): Pair<ActiveCharacter, ActiveCharacter> {
        val first = pair.first
        val second = pair.second

        val firstDamage = getFirstCharacterDamage(first, second)
        val secondDamage = getFirstCharacterDamage(second, first)

        val damagedFirstAttributes = Attributes(health = first.currentAttributes.health - secondDamage)
        val damagedSecondAttributes = Attributes(health = second.currentAttributes.health - firstDamage)

        val damagedFirst = ActiveCharacter(TileType.Player, first.currentAttributes + damagedFirstAttributes)
        val damagedSecond = ActiveCharacter(TileType.Monster, second.currentAttributes + damagedSecondAttributes)

        return Pair(damagedFirst, damagedSecond)
    }

    private fun getFirstCharacterDamage(first: ActiveCharacter, second: ActiveCharacter): Int {
        var attackBonus = 1
        if (isLuckHappend(first)) {
            attackBonus = 2
        }

        val pureDamage = first.currentAttributes.attack * attackBonus
        var finalDamage = pureDamage
        if (isDexterityHappened(second)) {
            finalDamage = 0
        }

        var absorbedDamage = finalDamage - second.currentAttributes.absorbingDamage(finalDamage)
        if (absorbedDamage < 0) {
            absorbedDamage = 0
        }

        return absorbedDamage
    }
}