package ru.spbau.shavkunov.roguelike.view

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import ru.spbau.shavkunov.roguelike.controller.InventoryController
import ru.spbau.shavkunov.roguelike.controller.MapController
import ru.spbau.shavkunov.roguelike.view.drawers.InventoryDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.LostGameDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.MapDrawer
import java.util.function.Consumer

class GameView {
    private val mapController = MapController()
    private val inventoryController = InventoryController()

    private val gameTitle = "Roguelike"
    private val terminal = TerminalBuilder
            .newBuilder()
            .initialTerminalSize(mapController.mapSize)
            .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
            .title(gameTitle)
            .build()

    init {
        terminalMapSize.size = mapController.mapSize

        terminal.onInput(Consumer {
            val screenType = mapController.process(it)
            val worldState = mapController.worldState

            when(screenType) {
                ScreenType.Map       -> MapDrawer.draw(terminal, mapController)
                ScreenType.Inventory -> InventoryDrawer.draw(terminal, inventoryController)
                ScreenType.LostGame  -> LostGameDrawer.draw(terminal, mapController)
            }
        })
    }
}

object terminalMapSize {
    var size: Size? = null
}