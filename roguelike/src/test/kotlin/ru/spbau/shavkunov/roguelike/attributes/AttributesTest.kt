package ru.spbau.shavkunov.roguelike.attributes

import org.junit.Assert.assertEquals
import org.junit.Test

class AttributesTest {
    private val zeroAttributes = Attributes()
    private val onesAttributes = Attributes(1, 1, 1, 1, 1)
    private val arbitraryAttributes = Attributes(1, 2, 3, 4, 5)

    @Test
    fun checkSum() {
        checkAttributesEquals(zeroAttributes, zeroAttributes + zeroAttributes)
        checkAttributesEquals(onesAttributes, onesAttributes + zeroAttributes)
        checkAttributesEquals(arbitraryAttributes, arbitraryAttributes + zeroAttributes)
    }

    @Test
    fun checkSubstraction() {
        checkAttributesEquals(zeroAttributes, zeroAttributes - zeroAttributes)
        checkAttributesEquals(Attributes(0, 1, 2, 3, 4), arbitraryAttributes - onesAttributes)
        checkAttributesEquals(Attributes(-1, -1, -1, -1, -1), zeroAttributes - onesAttributes)
    }
}

fun checkAttributesEquals(first: Attributes, second: Attributes) {
    assertEquals(first.attack,    second.attack)
    assertEquals(first.armor,     second.armor)
    assertEquals(first.luck,      second.luck)
    assertEquals(first.dexterity, second.dexterity)
    assertEquals(first.health,    second.health)
}