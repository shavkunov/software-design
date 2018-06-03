package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.color.TextColorFactory
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.GameMap
import ru.spbau.shavkunov.roguelike.gamestate.additionalRows

interface Drawer {
    fun draw(terminal: Terminal)
    fun getTerminalSize(): Size
}

fun getLayer(gameMap: GameMap, offset: Int = 0): Layer {

    return LayerBuilder.newBuilder()
            .size(Size.of(gameMap.columns, additionalRows))
            .offset(Position.of(offset, gameMap.rows))
            .build()
}

fun getLayerWithAttributes(title: String, gameMap: GameMap,
                           attributes: Attributes,
                           offset: Int = 0,
                           isTitleHighlight: Boolean = false
): Layer {
    val layer = getLayer(gameMap, offset)
    val text = attributes.getStringAttributes()

    if (isTitleHighlight) {
        layer.setForegroundColor(TextColorFactory.fromString("#ff0000"))
        layer.putText(title, Position.of(0, 0))

        layer.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR)
    } else {
        layer.putText(title, Position.of(0, 0))
    }

    for ((index, inventoryString) in text.withIndex()) {
        layer.putText(inventoryString, Position.of(0, index + 1))
    }

    return layer
}