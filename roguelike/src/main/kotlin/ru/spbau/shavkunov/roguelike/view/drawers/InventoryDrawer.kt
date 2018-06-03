package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.listener.InventoryListener
import ru.spbau.shavkunov.roguelike.view.TerminalFactory

class InventoryDrawer(private val listener: InventoryListener) : Drawer {
    val equippedItemsText = "Your equipment:"
    val unusedItemsText = "Your Inventory:"
    val help = "use enter to equip item"
    val attributesOffset = Attributes.getStandardOffset()
    val worldState = listener.getCurrentState()

    override fun getTerminalSize(): Size {
        val inventory = worldState.getPlayerInventory()
        val inventorySize = inventory.equippedItems.size + inventory.unusedItems.size
        val currentSize = Size.of(worldState.gameMap.columns, attributesOffset * inventorySize + 2)

        return currentSize
    }

    override fun draw(terminal: Terminal) {
        val inventory = worldState.getPlayerInventory()
        val currentSize = getTerminalSize()

        val equipLayer = getPanelLayer(currentSize, 0)
        equipLayer.putText(equippedItemsText)
        terminal.flush()

        var lastOffset = 0
        for ((index, item) in inventory.equippedItems.keys.withIndex()) {
            val inventoryItem = inventory.equippedItems[item]!!
            val itemAttributes = inventoryItem.attributes
            val itemTitle = inventoryItem.getTitle()

            lastOffset = index * attributesOffset
            val layer = getLayerWithAttributes(itemTitle, worldState.gameMap, itemAttributes, lastOffset)

            terminal.pushLayer(layer)
            terminal.flush()
        }

        val inventoryLayer = getPanelLayer(currentSize, lastOffset)
        inventoryLayer.putText(unusedItemsText)

        for ((index, item) in inventory.unusedItems.withIndex()) {
            val itemAttributes = item.attributes
            val itemTitle = item.getTitle()
            val isHighlight = index == listener.currentUnused

            lastOffset += index * attributesOffset
            val layer = getLayerWithAttributes(itemTitle, worldState.gameMap, itemAttributes, lastOffset, isHighlight)
            terminal.pushLayer(layer)
            terminal.flush()
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