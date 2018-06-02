package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.GameMap
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.additionalRows

object MapDrawer: Drawer {
    override fun draw(terminal: Terminal, worldState: WorldState) {
        val gameMap = worldState.gameMap
        for (row in 0 until gameMap.rows) {
            for (column in 0 until gameMap.columns) {
                val tile = gameMap.getTile(ru.spbau.shavkunov.roguelike.navigation.Position(row, column))
                terminal.setCharacterAt(
                        Position.of(column, row),
                        tile.value
                )
            }
        }

        val attributes = worldState.getPlayerAttributes()
        val layer = getLayerWithAttributes(gameMap, attributes)

        terminal.pushLayer(layer)
        terminal.flush()
    }
}