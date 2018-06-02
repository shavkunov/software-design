package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.gamestate.WorldState

object InventoryDrawer : Drawer {
    override fun draw(terminal: Terminal, worldState: WorldState) {
        val inventory = worldState.getPlayerInventory()

        for ((index, item) in inventory.equippedItems.keys.withIndex()) {
            val itemAttributes = inventory.equippedItems[item]!!.attributes
            val attributesOffset = itemAttributes.getStringAttributes().size + 2
            val layer = getLayerWithAttributes(worldState.gameMap, itemAttributes, index * attributesOffset)

            terminal.pushLayer(layer)
            terminal.flush()
        }
    }
}