package ru.spbau.shavkunov.roguelike.characters

import ru.spbau.shavkunov.roguelike.attributes.*
import ru.spbau.shavkunov.roguelike.gamestate.TileType

/**
 * Representing moving character on the board. At the moment, it's either monster or player.
 */
class ActiveCharacter(
        val tileType: TileType,
        updatedAttributes: Attributes? = null,
        createdBasicAttributes: Attributes? = null
) {
    private val basicAttributes: Attributes
    var currentAttributes: Attributes
    val inventory = Inventory()

    init {
        if (createdBasicAttributes == null) {
            when (tileType) {
                TileType.Player -> basicAttributes = getCharacterBasicAttributes()
                else -> basicAttributes = getMonsterBasicAttributes()
            }
        } else {
            basicAttributes = createdBasicAttributes
        }

        if (updatedAttributes != null) {
            currentAttributes = updatedAttributes
        } else {
            currentAttributes = basicAttributes
        }
    }

    fun pickItem(item: InventoryItem) {
        inventory.addItem(item)
    }

    fun equipItem(index: Int) {
        inventory.equipItem(index)
        updateCurrentAttributes()
    }

    private fun updateCurrentAttributes() {
        val attack = basicAttributes.attack
        val armor = basicAttributes.armor
        val luck = basicAttributes.luck
        val dexterity = basicAttributes.dexterity
        val health = currentAttributes.health // health doesn't regen

        currentAttributes = Attributes(attack, health, armor, luck, dexterity) + inventory.getBonusAttributes()
    }

    fun isDead(): Boolean {
        return currentAttributes.health <= 0
    }
}