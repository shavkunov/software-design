package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import org.slf4j.LoggerFactory
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.ScreenType

private val LOGGER = LoggerFactory.getLogger("InventoryListener")

/**
 * Listens to input during on Inventory Screen
 */
class InventoryListener(val worldState: WorldState) : Listener {
    var currentUnused = 0

    override fun process(input: Input): ScreenType {
        if (!input.isKeyStroke()) {
            return ScreenType.Inventory
        }

        val inputType = input.getInputType()

        return processInputType(inputType)
    }

    fun processInputType(inputType: InputType): ScreenType {
        var screenType = ScreenType.Inventory

        when(inputType) {
            InputType.ArrowDown  -> {
                LOGGER.info("Inventory: pressed down arrow")
                if (currentUnused + 1 < worldState.getPlayerInventory().unusedItems.size) {
                    currentUnused++
                }
            }
            InputType.ArrowUp    -> {
                LOGGER.info("Inventory: pressed up arrow")
                if (currentUnused - 1 >= 0) {
                    currentUnused--
                }
            }
            InputType.Enter      -> {
                LOGGER.info("Inventory: pressed enter")
                worldState.getPlayer().equipItem(currentUnused)
            }
            InputType.Escape     -> {
                LOGGER.info("Inventory: pressed escape")
                screenType = ScreenType.Map
            }
            else                 -> {}
        }

        return screenType
    }

    override fun getCurrentState(): WorldState {
        return worldState
    }
}