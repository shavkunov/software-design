package ru.spbau.shavkunov.roguelike

import ru.spbau.shavkunov.roguelike.attributes.*

open class Character(private val basicAttributes: Attributes) {
    private val inventory = Inventory()
    var currentAttributes = basicAttributes

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

class Player() : Character(getCharacterBasicAttributes()) {

}

class Monster() : Character(getMonsterBasicAttributes()) {

}