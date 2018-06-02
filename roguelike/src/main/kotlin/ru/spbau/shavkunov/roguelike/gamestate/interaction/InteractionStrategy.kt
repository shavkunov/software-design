package ru.spbau.shavkunov.roguelike.gamestate.interaction

import ru.spbau.shavkunov.roguelike.characters.ActiveCharacter
import ru.spbau.shavkunov.roguelike.gamestate.ObjectWithPosition
import ru.spbau.shavkunov.roguelike.navigation.Position

interface InteractionStrategy {
    fun proceedFloor()
    fun proceedMonster(monsters: MutableMap<Position, ActiveCharacter>)
    fun proceedPlayer(player: ObjectWithPosition<ActiveCharacter>)
    fun proceedLootboox()
}