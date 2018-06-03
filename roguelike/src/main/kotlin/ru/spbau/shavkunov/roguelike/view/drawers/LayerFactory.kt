package ru.spbau.shavkunov.roguelike.view.drawers

import javafx.geometry.Pos
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.color.TextColorFactory
import org.codetome.zircon.api.graphics.Layer
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.GameMap
import ru.spbau.shavkunov.roguelike.gamestate.additionalRows

/**
 * Creating layers with less parameters: only with size and offset
 */
object LayerFactory {
    fun getLayer(gameMap: GameMap, offset: Int = 0): Layer {
        val size = Size.of(gameMap.columns, additionalRows)
        val layerOffset = Position.of(offset, gameMap.rows)

        return getLayer(size, layerOffset)
    }

    fun getLayerWithAttributes(title: String, gameMap: GameMap,
                               attributes: Attributes,
                               offset: Int = 0,
                               isTitleHighlight: Boolean = false
    ): Layer {
        val layer = getLayer(gameMap, offset)
        return fillAttributes(layer, title, attributes, isTitleHighlight)
    }

    fun getAttributesWithSize(title: String,
                              size: Size,
                              attributes: Attributes,
                              offset: Int = 0,
                              isTitleHighlight: Boolean = false): Layer {
        val layer = getLayer(size, Position.of(0, offset))
        return fillAttributes(layer, title, attributes, isTitleHighlight)
    }

    fun getLayer(currentSize: Size, offset: Position): Layer {
        return LayerBuilder.newBuilder()
                .size(currentSize)
                .offset(offset)
                .build()
    }

    fun getLayer(currentSize: Size, verticalOffset: Int): Layer {
        return LayerBuilder.newBuilder()
                .size(currentSize)
                .offset(Position.of(0, verticalOffset))
                .build()
    }

    private fun fillAttributes(layer: Layer, title: String,
                               attributes: Attributes,
                               isTitleHighlight: Boolean = false
    ): Layer {
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
}