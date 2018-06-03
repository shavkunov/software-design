package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import org.codetome.zircon.api.input.KeyStroke
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up
import ru.spbau.shavkunov.roguelike.view.ScreenType

class MapListener: Listener {
    val worldState = WorldState()
    val mapSize = worldState.getMapSize()

    override fun process(input: Input): ScreenType {
        if (!input.isKeyStroke()) {
            return ScreenType.Map
        }

        val stroke = input.asKeyStroke()
        val inputType = input.getInputType()

        return processInputType(inputType, stroke)
    }

    fun processInputType(inputType: InputType, stroke: KeyStroke): ScreenType {
        var screenType = ScreenType.Map

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

        if (worldState.monstersLeft() == 0) {
            screenType = ScreenType.WinGame
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