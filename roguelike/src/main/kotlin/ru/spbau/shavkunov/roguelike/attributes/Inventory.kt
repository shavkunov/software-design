package ru.spbau.shavkunov.roguelike.attributes

import ru.spbau.shavkunov.roguelike.nextInt
import ru.spbau.shavkunov.roguelike.random

class Inventory {
    val unusedItems: MutableList<InventoryType> = mutableListOf()
    val equippedItems: MutableMap<String, InventoryType> = HashMap()

    fun addItem(item: InventoryType) {
        unusedItems.add(item)
    }

    fun equipItem(index: Int) {
        val item = unusedItems[index]
        val title = item.getTitle()
        unusedItems.removeAt(index)

        if (equippedItems.containsKey(title)) {
            unusedItems.add(equippedItems[title]!!)
        } else {
            equippedItems[item.getTitle()] = item
        }
    }

    fun getBonusAttributes(): Attributes {
        var attributes = Attributes()
        for (item: InventoryType in equippedItems.values) {
            attributes += item.attributes
        }

        return attributes
    }
}

abstract class InventoryType {
    open val attributes = Attributes()

    abstract fun getTitle(): String
}

class Helmet : InventoryType() {
    private val armorRange = 5..10
    private val dexterityRange = 0..5
    override val attributes = Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))

    override fun getTitle(): String {
        return "helmet"
    }
}

class Gloves : InventoryType() {
    private val armorRange = 1..5
    private val dexterityRange = 0..10
    override val attributes = Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))

    override fun getTitle(): String {
        return "gloves"
    }
}

class Boots : InventoryType() {
    private val armorRange = 5..10
    private val dexterityRange = 10..15
    override val attributes = Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))

    override fun getTitle(): String {
        return "boots"
    }
}

class Armor : InventoryType() {
    private val armorRange = 15..20
    private val dexterityRange = -2..5
    override val attributes = Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))

    override fun getTitle(): String {
        return "armor"
    }
}

class Amulet : InventoryType() {
    private val luckRange = 5..10
    override val attributes = Attributes(luck = random.nextInt(luckRange))

    override fun getTitle(): String {
        return "amulet"
    }
}

class Weapon : InventoryType() {
    private val attack = 3..5
    override val attributes = Attributes(attack = random.nextInt(attack))

    override fun getTitle(): String {
        return "weapon"
    }
}

fun generateItem(): InventoryType {
    when(random.nextInt(6)) {
        0 -> return Helmet()
        1 -> return Gloves()
        2 -> return Boots()
        3 -> return Armor()
        4 -> return Amulet()
        5 -> return Weapon()

        else -> throw RuntimeException()
    }
}