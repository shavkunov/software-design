package ru.spbau.shavkunov.roguelike.characters

import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.TileType
import ru.spbau.shavkunov.roguelike.isDexterityHappened
import ru.spbau.shavkunov.roguelike.isLuckHappend

object CombatResolver {
    fun resolveCombat(
            pair: Pair<ActiveCharacter, ActiveCharacter>,
            ignoreBonuses: Boolean = false
    ): Pair<ActiveCharacter, ActiveCharacter> {
        val first = pair.first
        val second = pair.second

        val firstDamage = getFirstCharacterDamage(first, second, ignoreBonuses)
        val secondDamage = getFirstCharacterDamage(second, first, ignoreBonuses)

        val damagedFirstAttributes = Attributes(health = secondDamage)
        val damagedSecondAttributes = Attributes(health = firstDamage)

        val damagedFirst = ActiveCharacter(TileType.Player, first.currentAttributes - damagedFirstAttributes)
        val damagedSecond = ActiveCharacter(TileType.Monster, second.currentAttributes - damagedSecondAttributes)

        return Pair(damagedFirst, damagedSecond)
    }

    private fun getFirstCharacterDamage(first: ActiveCharacter, second: ActiveCharacter, ignoreBonuses: Boolean): Int {
        var attackBonus = 1
        if (isLuckHappend(first) && !ignoreBonuses) {
            attackBonus = 2
        }

        val pureDamage = first.currentAttributes.attack * attackBonus
        var finalDamage = pureDamage
        if (isDexterityHappened(second) && !ignoreBonuses) {
            finalDamage = 0
        }

        var absorbedDamage: Double = finalDamage.toDouble() - second.currentAttributes.absorbingDamage(finalDamage)
        if (absorbedDamage < 1) {
            absorbedDamage = 0.0
        }

        if (ignoreBonuses) {
            return finalDamage
        }

        if (second.currentAttributes.health == 1) {
            return 1
        }

        return absorbedDamage.toInt()
    }
}