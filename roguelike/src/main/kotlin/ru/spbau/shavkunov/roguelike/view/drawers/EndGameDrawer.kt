package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.listener.MapListener

class EndGameDrawer(val finalMessage: String, val listener: MapListener) : Drawer {
    override fun getTerminalSize(): Size {
        return listener.mapSize
    }

    override fun draw(terminal: Terminal) {
        val worldState = listener.getCurrentState()
        val row = worldState.gameMap.rows / 2
        val column = (worldState.gameMap.columns - finalMessage.length) / 2

        terminal.clear()
        var currentCharPosition = column - finalMessage.length/2
        for (char in finalMessage) {
            terminal.setCharacterAt(Position.of(currentCharPosition, row), char)
            currentCharPosition += 1
        }
    }
}