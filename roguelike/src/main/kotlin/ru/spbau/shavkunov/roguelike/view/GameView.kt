package ru.spbau.shavkunov.roguelike.view

import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import ru.spbau.shavkunov.roguelike.listener.InventoryListener
import ru.spbau.shavkunov.roguelike.listener.Listener
import ru.spbau.shavkunov.roguelike.listener.MapListener
import ru.spbau.shavkunov.roguelike.view.drawers.EndGameDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.InventoryDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.MapDrawer
import java.util.function.Consumer

class GameView {
    private val lostMessage = "You died!"
    private val winMessage = "You won!"

    private val mapListener = MapListener()
    private val inventoryListener = InventoryListener(mapListener.worldState)

    private val mapDrawer = MapDrawer(mapListener)
    private val inventoryDrawer = InventoryDrawer(inventoryListener)
    private var activeListener: Listener = mapListener

    private val gameTitle = "Roguelike"
    private val terminal = TerminalBuilder
            .newBuilder()
            .initialTerminalSize(mapListener.mapSize)
            .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
            .title(gameTitle)
            .build()

    init {
        terminal.onInput(Consumer {
            val screenType = activeListener.process(it)

            terminal.clear()
            when(screenType) {
                ScreenType.Map       -> mapDrawer.draw(terminal)
                ScreenType.Inventory -> inventoryDrawer.draw(terminal)
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