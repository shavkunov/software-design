package ru.spbau.shavkunov.roguelike.characters

import ru.spbau.shavkunov.roguelike.attributes.*
import ru.spbau.shavkunov.roguelike.gamestate.TileType

class ActiveCharacter(
        val tileType: TileType,
        val updatedAttributes: Attributes? = null
) {
    private val basicAttributes: Attributes
    var currentAttributes: Attributes
    val inventory = Inventory()

    init {
        when (tileType) {
            TileType.Player -> basicAttributes = getCharacterBasicAttributes()
            else -> basicAttributes = getMonsterBasicAttributes()
        }

        if (updatedAttributes != null) {
            currentAttributes = updatedAttributes
        } else {
            currentAttributes = basicAttributes
        }
    }

    fun pickItem(item: InventoryType) {
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
        return currentAttributes.health == 0
    }
}