package ru.spbau.shavkunov.roguelike

import ru.spbau.shavkunov.roguelike.attributes.Attributes

object CombatResolver {
    fun resolveCombat(pair: Pair<Character, Character>): Pair<Character, Character> {
        val first = pair.first
        val second = pair.second

        val firstDamage = getFirstCharacterDamage(first, second)
        val secondDamage = getFirstCharacterDamage(second, first)

        val damagedFirstAttributes = Attributes(health = first.currentAttributes.health - secondDamage)
        val damagedSecondAttributes = Attributes(health = second.currentAttributes.health - firstDamage)

        val damagedFirst = Character(first.currentAttributes + damagedFirstAttributes)
        val damagedSecond = Character(second.currentAttributes + damagedSecondAttributes)

        return Pair(damagedFirst, damagedSecond)
    }

    private fun getFirstCharacterDamage(first: Character, second: Character): Int {
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