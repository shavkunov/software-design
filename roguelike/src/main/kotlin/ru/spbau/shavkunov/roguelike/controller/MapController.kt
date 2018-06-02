package ru.spbau.shavkunov.roguelike.controller

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up
import ru.spbau.shavkunov.roguelike.view.ScreenType

val additionalRows = 10

class MapController: Controller {
    val worldState = WorldState()
    val mapSize = Size.of(
            worldState.gameMap.columns,
            worldState.gameMap.rows + additionalRows)

    override fun process(input: Input): ScreenType {
        var screenType = ScreenType.Map

        if (!input.isKeyStroke()) {
            return screenType
        }

        val stroke = input.asKeyStroke()
        val inputType = input.getInputType()

        when(inputType) {
            InputType.ArrowLeft  -> worldState.playerUpdate(Left)
            InputType.ArrowDown  -> worldState.playerUpdate(Down)
            InputType.ArrowRight -> worldState.playerUpdate(Right)
            InputType.ArrowUp    -> worldState.playerUpdate(Up)

            InputType.Character  -> when(stroke.getCharacter()) {
                'I', 'i' -> {
                    screenType = ScreenType.Inventory
                }
                else -> {}
            }

            InputType.Escape     -> {
                if (screenType == ScreenType.Inventory) {
                    screenType = ScreenType.Map
                }
            }

            else -> {}
        }

        if (worldState.isPlayerDead()) {
            screenType = ScreenType.LostGame
        }

        return screenType
    }

    override fun getCurrentState(): WorldState {
        return worldState
    }
}