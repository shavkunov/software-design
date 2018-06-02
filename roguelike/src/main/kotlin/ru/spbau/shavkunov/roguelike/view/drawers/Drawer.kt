package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.GameMap
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.GameView
import ru.spbau.shavkunov.roguelike.view.additionalRows

interface Drawer {
    fun draw(terminal: Terminal, worldState: WorldState)
}

fun getLayerWithAttributes(gameMap: GameMap, attributes: Attributes, offset: Int = 0): Layer {
    val layer = LayerBuilder.newBuilder()
            .size(Size.of(gameMap.columns, additionalRows))
            .offset(Position.of(offset, gameMap.rows))
            .build()

    val text = attributes.getStringAttributes()
    for ((index, inventoryString) in text.withIndex()) {
        layer.putText(inventoryString, Position.of(0, index))
    }

    return layer
}