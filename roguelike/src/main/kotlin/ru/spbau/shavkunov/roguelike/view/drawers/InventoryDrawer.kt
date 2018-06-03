package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.listener.InventoryListener
import ru.spbau.shavkunov.roguelike.view.TerminalFactory
import ru.spbau.shavkunov.roguelike.view.drawers.LayerFactory.getAttributesWithSize
import ru.spbau.shavkunov.roguelike.view.drawers.LayerFactory.getLayer
import ru.spbau.shavkunov.roguelike.view.drawers.LayerFactory.getLayerWithAttributes

class InventoryDrawer(private val listener: InventoryListener) : Drawer {
    val equippedItemsText = "Your equipment:"
    val unusedItemsText = "Your Inventory:"
    val help = "use enter to equip item"
    val attributesOffset = Attributes.getStandardOffset()
    val worldState = listener.getCurrentState()

    override fun getTerminalSize(): Size {
        val inventory = worldState.getPlayerInventory()
        val inventorySize = inventory.equippedItems.size + inventory.unusedItems.size
        val currentSize = Size.of(worldState.gameMap.columns, attributesOffset * inventorySize + 5)

        return currentSize
    }

    override fun draw(terminal: Terminal) {
        val inventory = worldState.getPlayerInventory()
        val currentSize = getTerminalSize()

        val equipLayer = getLayer(currentSize, 0)
        equipLayer.putText(equippedItemsText)
        terminal.pushLayer(equipLayer)
        terminal.flush()

        var lastOffset = 0
        for ((index, item) in inventory.equippedItems.keys.withIndex()) {
            val inventoryItem = inventory.equippedItems[item]!!
            val itemAttributes = inventoryItem.attributes
            val itemTitle = inventoryItem.getTitle()

            lastOffset = index * attributesOffset
            val layer = getAttributesWithSize(itemTitle, currentSize, itemAttributes, lastOffset)

            terminal.pushLayer(layer)
            terminal.flush()
        }

        var inventoryLayer = getLayer(currentSize, lastOffset)
        if (lastOffset == 0) {
            inventoryLayer = getLayer(currentSize, lastOffset + 1)
        }

        inventoryLayer.putText(unusedItemsText)
        terminal.pushLayer(inventoryLayer)
        terminal.flush()

        for ((index, item) in inventory.unusedItems.withIndex()) {
            val itemAttributes = item.attributes
            val itemTitle = item.getTitle()
            val isHighlight = index == listener.currentUnused

            lastOffset += index * attributesOffset
            val layer =  getAttributesWithSize(itemTitle, currentSize, itemAttributes, lastOffset, isHighlight)
            terminal.pushLayer(layer)
            terminal.flush()
        }

        var helpLayer = getLayer(currentSize, lastOffset)
        if (lastOffset == 0) {
            helpLayer = getLayer(currentSize, lastOffset + 2)
        }
        helpLayer.putText(help)
        terminal.pushLayer(helpLayer)
        terminal.flush()
    }
}