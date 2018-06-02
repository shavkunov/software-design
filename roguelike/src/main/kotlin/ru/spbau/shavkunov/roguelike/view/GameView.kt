package ru.spbau.shavkunov.roguelike.view

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import ru.spbau.shavkunov.roguelike.controller.GameController
import ru.spbau.shavkunov.roguelike.view.drawers.InventoryDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.LostGameDrawer
import ru.spbau.shavkunov.roguelike.view.drawers.MapDrawer
import java.util.function.Consumer

val additionalRows = 10

class GameView {
    private val controller = GameController()
    private val gameTitle = "Roguelike"
    private val terminal = TerminalBuilder
            .newBuilder()
            .initialTerminalSize(Size.of(
                    controller.worldState.gameMap.columns,
                    controller.worldState.gameMap.rows + additionalRows))
            .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
            .title(gameTitle)
            .build()

    init {
        terminal.onInput(Consumer {
            val screenType = controller.process(it)
            val worldState = controller.worldState

            when(screenType) {
                ScreenType.Map       -> MapDrawer.draw(terminal, worldState)
                ScreenType.Inventory -> InventoryDrawer.draw(terminal, worldState)
                ScreenType.LostGame  -> LostGameDrawer.draw(terminal, worldState)

                ScreenType.Quit -> terminal.close()
            }
        })
    }
}