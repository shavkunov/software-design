package ru.spbau.shavkunov.roguelike.view.drawers

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.terminal.Terminal

/**
 * Drawar, which is drawing something in provided terminal
 */
interface Drawer {
    fun draw(terminal: Terminal)
    fun getTerminalSize(): Size
}