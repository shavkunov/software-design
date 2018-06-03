package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.input.InputType
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.ScreenType

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
                if (currentUnused + 1 < worldState.getPlayerInventory().unusedItems.size) {
                    currentUnused++
                }
            }
            InputType.ArrowUp    -> {
                if (currentUnused - 1 >= 0) {
                    currentUnused--
                }
            }
            InputType.Enter      -> {
                worldState.getPlayer().equipItem(currentUnused)
            }
            InputType.Escape     -> screenType = ScreenType.Map
            else                 -> {}
        }

        return screenType
    }

    override fun getCurrentState(): WorldState {
        return worldState
    }
}