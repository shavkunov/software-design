package ru.spbau.shavkunov.roguelike.attributes

import junit.framework.Assert.assertEquals
import org.junit.Test

class InventoryTest {
    val helmetAttributes = Attributes(1, 2, 3, 4, 5)
    val armorAttributes = Attributes(0, 0, 5, 0, 0)
    val helmet = Helmet(helmetAttributes)
    val anotherHelmet = Helmet()
    val armor = Armor(armorAttributes)

    @Test
    fun testEquipping() {
        val inventory = Inventory()
        inventory.addItem(helmet)
        inventory.addItem(armor)

        assertEquals(helmet, inventory.unusedItems[0])
        assertEquals(armor, inventory.unusedItems[1])

        inventory.equipItem(0)
        assertEquals(helmet, inventory.equippedItems[helmet.getTitle()])

        inventory.equipItem(0)
        assertEquals(armor, inventory.equippedItems[armor.getTitle()])

        inventory.addItem(anotherHelmet)
        inventory.equipItem(0)

        assertEquals(helmet, inventory.unusedItems[0])
        assertEquals(anotherHelmet, inventory.equippedItems[anotherHelmet.getTitle()])
    }

    @Test
    fun bonusAttributesTest() {
        val inventory = Inventory()
        inventory.addItem(helmet)
        inventory.addItem(armor)
        inventory.equipItem(0)
        inventory.equipItem(0)

        checkAttributesEquals(Attributes(1 , 2, 8, 4, 5), inventory.getBonusAttributes())
    }
}