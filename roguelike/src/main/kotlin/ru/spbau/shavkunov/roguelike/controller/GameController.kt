package ru.spbau.shavkunov.roguelike.controller

import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up
import ru.spbau.shavkunov.roguelike.view.ScreenType

class GameController {
    val worldState = WorldState()

    fun process(input: Input): ScreenType {
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
                'Q', 'q' -> {
                    if (screenType == ScreenType.Map) {
                        screenType = ScreenType.Quit
                    }
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
}