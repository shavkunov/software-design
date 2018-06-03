package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.listener.MapListener

class MapDrawer(private val listener: MapListener): Drawer {
    val inventoryHelp = "Press I to open your inventory"
    val stateCommentary = "Your stats:"

    override fun getTerminalSize(): Size {
        return listener.mapSize
    }

    override fun draw(terminal: Terminal) {
        val worldState = listener.getCurrentState()

        terminal.setSize(getTerminalSize())

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
        val layer = getLayerWithAttributes(stateCommentary, gameMap, attributes)

        terminal.pushLayer(layer)

        val offset = Attributes.getStandardOffset()
        val helpLayer = getLayer(gameMap)
        helpLayer.putText(inventoryHelp, Position.of(0, offset))

        terminal.pushLayer(helpLayer)
        terminal.flush()
    }
}