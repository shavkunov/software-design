package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import org.codetome.zircon.api.input.KeyStroke
import org.slf4j.LoggerFactory
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.navigation.Down
import ru.spbau.shavkunov.roguelike.navigation.Left
import ru.spbau.shavkunov.roguelike.navigation.Right
import ru.spbau.shavkunov.roguelike.navigation.Up
import ru.spbau.shavkunov.roguelike.view.ScreenType

private val LOGGER = LoggerFactory.getLogger("MapListener")

/**
 * Listens to input during on Map Screen
 */
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
            InputType.ArrowLeft  -> {
                LOGGER.info("Map: pressed left arrow")
                worldState.playerUpdate(Left)
            }

            InputType.ArrowDown  -> {
                LOGGER.info("Map: pressed down arrow")
                worldState.playerUpdate(Down)
            }

            InputType.ArrowRight -> {
                LOGGER.info("Map: pressed right arrow")
                worldState.playerUpdate(Right)
            }

            InputType.ArrowUp    -> {
                LOGGER.info("Map: pressed up arrow")
                worldState.playerUpdate(Up)
            }

            InputType.Character  -> when(stroke.getCharacter()) {
                'I', 'i' -> {
                    LOGGER.info("Map: opening player's inventory")
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
            LOGGER.info("There is no monsters left!")
            screenType = ScreenType.WinGame
        }

        if (worldState.isPlayerDead()) {
            LOGGER.info("Player is dead")
            screenType = ScreenType.LostGame
        }

        return screenType
    }

    override fun getCurrentState(): WorldState {
        return worldState
    }
}