package ru.spbau.shavkunov.roguelike.attributes

import ru.spbau.shavkunov.roguelike.nextInt
import ru.spbau.shavkunov.roguelike.random

data class Attributes(
        val attack: Int = 0,
        val health: Int = 0,
        val armor: Int = 0,
        val luck: Int = 0,
        val dexterity: Int = 0) {
    operator fun plus(another: Attributes): Attributes {
        val newAttack = attack + another.attack
        val newHealth = health + another.health
        val newArmor = armor + another.armor
        val newLuck = luck + another.luck
        val newDexterity = dexterity + another.dexterity

        return Attributes(newAttack, newHealth, newArmor, newLuck, newDexterity)
    }

    operator fun minus(another: Attributes): Attributes {
        val oppositeAttributes = Attributes(
                -another.attack,
                -another.health,
                -another.armor,
                -another.luck,
                -another.dexterity)

        return plus(oppositeAttributes)
    }

    fun getStringAttributes(): List<String> {
        val attackString    = "Attack: $attack"
        val healthString    = "Health: $health"
        val armorString     = "Armor: $armor"
        val luckString      = "Luck: $luck"
        val dexterityString = "Dexterity: $dexterity"

        return listOf(attackString, healthString, armorString, luckString, dexterityString)
    }

    fun absorbingDamage(damage: Int): Int {
        return damage * armor / 100
    }
}

val maxLuck = 100
val maxDexterity = 100
val playerAttack = 5
val playerLuckRange = 0..5
val playerDexterityRange = 10..50
const val playerBasicHealth = 10
const val playerBasicArmor = 10

fun getCharacterBasicAttributes(attack: Int = playerAttack,
                                luckRange: IntRange = playerLuckRange,
                                dexterityRange: IntRange = playerDexterityRange,
                                health: Int = playerBasicHealth,
                                armor: Int = playerBasicArmor): Attributes {
    val luck = random.nextInt(luckRange)
    val dexterity = random.nextInt(dexterityRange)
    return Attributes(attack, health, armor, luck, dexterity)
}

fun getMonsterBasicAttributes(): Attributes {
    val attack = playerAttack / 3
    val luckRange = playerLuckRange.first..playerLuckRange.last/2
    val dexterityRange = playerDexterityRange.first..playerDexterityRange.last/2
    val health = playerBasicHealth
    val armor = playerBasicArmor / 2

    return getCharacterBasicAttributes(attack, luckRange, dexterityRange, health, armor)
}