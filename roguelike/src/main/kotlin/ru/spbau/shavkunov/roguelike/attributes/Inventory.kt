package ru.spbau.shavkunov.roguelike.attributes

import ru.spbau.shavkunov.roguelike.nextInt
import ru.spbau.shavkunov.roguelike.random

/**
 * Inventory stores items of the character. It's keeping unused and equiped items.
 */
class Inventory {
    val unusedItems: MutableList<InventoryItem> = mutableListOf()
    val equippedItems: MutableMap<String, InventoryItem> = HashMap()

    fun addItem(item: InventoryItem) {
        unusedItems.add(item)
    }

    fun equipItem(index: Int) {
        val item = unusedItems[index]
        val title = item.getTitle()
        unusedItems.removeAt(index)

        if (equippedItems.containsKey(title)) {
            unusedItems.add(equippedItems[title]!!)
        }

        equippedItems[item.getTitle()] = item
    }

    fun getBonusAttributes(): Attributes {
        var attributes = Attributes()
        for (item: InventoryItem in equippedItems.values) {
            attributes += item.attributes
        }

        return attributes
    }
}

/**
 * Item is a member of inventory. No more than one item of same type(see below) can be equiped simultaneously.
 */
abstract class InventoryItem(preparedAttributes: Attributes?) {
    val attributes: Attributes

    init {
        if (preparedAttributes != null) {
            attributes = preparedAttributes
        } else {
            attributes = initAttributes()
        }
    }

    abstract fun getTitle(): String
    abstract fun initAttributes(): Attributes
}

/**
 * Helmet for the head
 */
class Helmet(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val armorRange = 5..10
        val dexterityRange = 0..5

        return Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))
    }

    override fun getTitle(): String {
        return "helmet"
    }
}

/**
 * Gloves for the hands
 */
class Gloves(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val armorRange = 1..5
        val dexterityRange = 0..10

        return Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))
    }

    override fun getTitle(): String {
        return "gloves"
    }
}

/**
 * Boots for the foot
 */
class Boots(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val armorRange = 5..10
        val dexterityRange = 10..15

        return Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))
    }

    override fun getTitle(): String {
        return "boots"
    }
}

/**
 * Armor for the body
 */
class Armor(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val armorRange = 15..20
        val dexterityRange = -2..5

        return Attributes(armor = random.nextInt(armorRange), dexterity = random.nextInt(dexterityRange))
    }

    override fun getTitle(): String {
        return "armor"
    }
}

/**
 * Amulet gives you a good luck!
 */
class Amulet(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val luckRange = 5..10

        return Attributes(luck = random.nextInt(luckRange))
    }

    override fun getTitle(): String {
        return "amulet"
    }
}

/**
 * Enhance dealing damage
 */
class Weapon(preparedAttributes: Attributes? = null) : InventoryItem(preparedAttributes) {
    override fun initAttributes(): Attributes {
        val attack = 3..5

        return Attributes(attack = random.nextInt(attack))
    }

    override fun getTitle(): String {
        return "weapon"
    }
}

fun generateItem(): InventoryItem {
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