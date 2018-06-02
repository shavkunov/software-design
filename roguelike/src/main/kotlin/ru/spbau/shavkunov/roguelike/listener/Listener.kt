package ru.spbau.shavkunov.roguelike.listener

import org.codetome.zircon.api.input.Input
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.ScreenType

interface Listener {
    fun process(input: Input): ScreenType
    fun getCurrentState(): WorldState
}