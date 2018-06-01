package ru.spbau.shavkunov.roguelike

import ru.spbau.shavkunov.roguelike.attributes.maxDexterity
import ru.spbau.shavkunov.roguelike.attributes.maxLuck
import java.util.*

val random = Random()

fun Random.nextInt(range: IntRange): Int {
    return range.start + nextInt(range.last - range.start)
}

fun isAttributeHappened(attribute: Int, maxValue: Int): Boolean {
    return random.nextInt(maxValue) + 1 <= attribute
}

fun isLuckHappend(character: Character): Boolean {
    return isAttributeHappened(character.currentAttributes.luck, maxLuck)
}

fun isDexterityHappened(character: Character): Boolean {
    return isAttributeHappened(character.currentAttributes.dexterity, maxDexterity)
}