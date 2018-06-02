package ru.spbau.shavkunov.roguelike.controller

import org.codetome.zircon.api.input.Input
import ru.spbau.shavkunov.roguelike.gamestate.WorldState
import ru.spbau.shavkunov.roguelike.view.ScreenType

interface Controller {
    fun process(input: Input): ScreenType
    fun getCurrentState(): WorldState
}