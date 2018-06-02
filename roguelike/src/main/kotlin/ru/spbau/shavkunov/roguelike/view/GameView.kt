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
    private val inventoryController = InventoryController(mapController.worldState)

    private val mapDrawer = MapDrawer(mapController)
    private val inventoryDrawer = InventoryDrawer(inventoryController)
    private val lostGameDrawer = LostGameDrawer(mapController)

    private val gameTitle = "Roguelike"
    private val terminal = TerminalBuilder
            .newBuilder()
            .initialTerminalSize(mapController.mapSize)
            .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
            .title(gameTitle)
            .build()

    init {
        terminal.onInput(Consumer {
            val screenType = mapController.process(it)
            val worldState = mapController.worldState

            when(screenType) {
                ScreenType.Map       -> mapDrawer.draw(terminal)
                ScreenType.Inventory -> inventoryDrawer.draw(terminal)
                ScreenType.LostGame  -> lostGameDrawer.draw(terminal)
            }
        })
    }
}