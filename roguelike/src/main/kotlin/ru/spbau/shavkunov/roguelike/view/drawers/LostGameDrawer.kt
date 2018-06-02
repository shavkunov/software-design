package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.LayerBuilder
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.controller.Controller
import ru.spbau.shavkunov.roguelike.gamestate.WorldState

object LostGameDrawer : Drawer {
    private val text = "You died!"

    override fun draw(terminal: Terminal, controller: Controller) {
        val worldState = controller.getCurrentState()
        val row = worldState.gameMap.rows / 2
        val column = (worldState.gameMap.columns - text.length) / 2

        terminal.clear()
        var currentCharPosition = column - text.length/2
        for (char in text) {
            terminal.setCharacterAt(Position.of(currentCharPosition, row), char)
            currentCharPosition += 1
        }
    }
}