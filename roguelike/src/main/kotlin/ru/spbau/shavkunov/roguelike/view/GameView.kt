package ru.spbau.shavkunov.roguelike.view

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.shavkunov.roguelike.listener.InventoryListener
import ru.spbau.shavkunov.roguelike.listener.Listener
import ru.spbau.shavkunov.roguelike.listener.MapListener
import ru.spbau.shavkunov.roguelike.view.drawers.EndGameDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.InventoryDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.MapDrawer
import java.util.function.Consumer

/**
 * Main class responsible for handling listeners responses and sending draw commands to drawers
 */
class GameView {
    private val lostMessage = "You died!"
    private val winMessage = "You won!"

    private val mapListener = MapListener()
    private val inventoryListener = InventoryListener(mapListener.worldState)

    private val mapDrawer = MapDrawer(mapListener)
    private val inventoryDrawer = InventoryDrawer(inventoryListener)
    private var activeListener: Listener = mapListener
    private var terminal = TerminalFactory.getTerminalWithSize(mapListener.mapSize)

    init {
        terminal.onInput(Consumer {
            val screenType = activeListener.process(it)

            terminal.clear()
            terminal.drainLayers()
            when(screenType) {
                ScreenType.Map       -> {
                    if (activeListener != mapListener) {
                        val prevTerminal = terminal
                        terminal = TerminalFactory.getTerminalWithSize(mapDrawer.getTerminalSize())
                        prevTerminal.close()
                    }

                    mapDrawer.draw(terminal)
                }
                ScreenType.Inventory -> {
                    if (activeListener != inventoryListener) {
                        val prevTerminal = terminal
                        terminal = TerminalFactory.getTerminalWithSize(inventoryDrawer.getTerminalSize())
                        prevTerminal.close()
                    }

                    inventoryDrawer.draw(terminal)
                }
                ScreenType.LostGame  -> EndGameDrawer(lostMessage, mapListener).draw(terminal)
                ScreenType.WinGame   -> EndGameDrawer(winMessage, mapListener).draw(terminal)
            }

            if (screenType == ScreenType.Inventory) {
                activeListener = inventoryListener
            } else {
                activeListener = mapListener
            }
        })
    }
}

object TerminalFactory {
    private const val gameTitle = "Roguelike"

    fun getTerminalWithSize(size: Size): Terminal {
        return TerminalBuilder
                .newBuilder()
                .initialTerminalSize(size)
                .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
                .title(gameTitle)
                .build()
    }
}