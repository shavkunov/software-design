package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.input.InputType
import org.codetome.zircon.api.input.KeyStroke
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spbau.shavkunov.roguelike.view.ScreenType

class MapListenerTest {
    val inputTypes = listOf(
            InputType.ArrowLeft, InputType.ArrowRight,
            InputType.ArrowUp, InputType.ArrowDown)

    @Test
    fun mapListenerTest() {
        val listener = MapListener()
        for (inputType in inputTypes) {
            val randomKeyStroke = KeyStroke.EOF_STROKE
            val screenType = listener.processInputType(inputType, randomKeyStroke)

            assertEquals(ScreenType.Map, screenType)
        }

        val stroke = KeyStroke('I')
        val screenType = listener.processInputType(InputType.Character, stroke)
        assertEquals(ScreenType.Inventory, screenType)
    }
}