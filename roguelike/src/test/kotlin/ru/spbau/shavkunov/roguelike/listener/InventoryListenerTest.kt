package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.input.InputType
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spbau.shavkunov.roguelike.attributes.Armor
import ru.spbau.shavkunov.roguelike.attributes.Helmet
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.ScreenType

class InventoryListenerTest {
    val inputTypes = listOf(
            InputType.Escape, InputType.Enter,
            InputType.ArrowUp, InputType.ArrowDown)

    @Test
    fun listenerTest() {
        val worldState = WorldState()
        worldState.getPlayer().inventory.addItem(Helmet())
        worldState.getPlayer().inventory.addItem(Armor())
        worldState.getPlayer().inventory.addItem(Armor())

        for (inputType in inputTypes) {
            val listener = InventoryListener(worldState)
            val screenType = listener.processInputType(inputType)

            when(inputType) {
                InputType.ArrowDown  -> {
                    assertEquals(ScreenType.Inventory, screenType)
                    assertEquals(1, listener.currentUnused)
                }

                InputType.ArrowUp    -> {
                    assertEquals(ScreenType.Inventory, screenType)
                    assertEquals(0, listener.currentUnused)
                }
                InputType.Enter      -> assertEquals(ScreenType.Inventory, screenType)
                InputType.Escape     -> assertEquals(ScreenType.Map, screenType)
                else                 -> {}
            }
        }

        val listener = InventoryListener(worldState)
        listener.processInputType(InputType.ArrowDown)
        listener.processInputType(InputType.ArrowDown)
        assertEquals(2, listener.currentUnused)
        listener.processInputType(InputType.ArrowDown)
        assertEquals(2, listener.currentUnused)
    }
}