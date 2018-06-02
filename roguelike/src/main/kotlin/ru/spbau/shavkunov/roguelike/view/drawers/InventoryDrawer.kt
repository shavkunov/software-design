package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.controller.Controller
import ru.spbau.shavkunov.roguelike.controller.InventoryController
import ru.spbau.shavkunov.roguelike.controller.additionalRows
import ru.spbau.shavkunov.roguelike.gamestate.WorldState

class InventoryDrawer(val controller: InventoryController) : Drawer {
    val equippedItemsText = "Your equipment:"
    val unusedItemsText = "Your Inventory:"
    val help = "use enter to equip item"
    val attributesOffset = Attributes.getStandardOffset()
    val worldState = controller.getCurrentState()
    val currentUnused = 0

    override fun getTerminalSize(): Size {
        val inventory = worldState.getPlayerInventory()
        val inventorySize = inventory.equippedItems.size + inventory.unusedItems.size
        val currentSize = Size.of(worldState.gameMap.columns, attributesOffset * inventorySize + 2)

        return currentSize
    }

    override fun draw(terminal: Terminal) {
        val inventory = worldState.getPlayerInventory()
        val currentSize = getTerminalSize()

        terminal.clear()
        terminal.setSize(currentSize)

        val equipLayer = getPanelLayer(currentSize, 0)
        equipLayer.putText(equippedItemsText)

        var lastOffset = 0
        for ((index, item) in inventory.equippedItems.keys.withIndex()) {
            val inventoryItem = inventory.equippedItems[item]!!
            val itemAttributes = inventoryItem.attributes
            val itemTitle = inventoryItem.getTitle()

            lastOffset = index * attributesOffset
            val layer = getLayerWithAttributes(itemTitle, worldState.gameMap, itemAttributes, lastOffset)

            terminal.pushLayer(layer)
        }

        val inventoryLayer = getPanelLayer(currentSize, lastOffset)
        inventoryLayer.putText(unusedItemsText)

        for ((index, item) in inventory.unusedItems.withIndex()) {
            val itemAttributes = item.attributes
            val itemTitle = item.getTitle()
            val isHighlight = index == currentUnused

            lastOffset += index * attributesOffset
            val layer = getLayerWithAttributes(itemTitle, worldState.gameMap, itemAttributes, lastOffset, isHighlight)
            terminal.pushLayer(layer)
        }

        val helpLayer = getPanelLayer(currentSize, lastOffset)
        helpLayer.putText(help)

        terminal.flush()
    }

    private fun getPanelLayer(currentSize: Size, offset: Int): Layer {
        return LayerBuilder.newBuilder()
                .size(currentSize)
                .offset(Position.of(0, offset))
                .build()
    }
}