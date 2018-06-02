package ru.spbau.shavkunov.roguelike.controller

import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up
import ru.spbau.shavkunov.roguelike.view.ScreenType

class InventoryController(val worldState: WorldState) : Controller {
    override fun process(input: Input): ScreenType {
        var screenType = ScreenType.Inventory

        if (!input.isKeyStroke()) {
            return screenType
        }

        val inputType = input.getInputType()
        when(inputType) {
            InputType.ArrowLeft  -> {}
            InputType.ArrowDown  -> {}
            InputType.ArrowRight -> {}
            InputType.ArrowUp    -> {}
            InputType.Enter      -> {}
            InputType.Escape     -> screenType = ScreenType.Map
            else                 -> {}
        }

        return screenType
    }

    override fun getCurrentState(): WorldState {
        return worldState
    }
}