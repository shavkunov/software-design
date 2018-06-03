package ru.spbau.shavkunov.roguelike.characters

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import ru.spbau.shavkunov.roguelike.attributes.Attributes
import ru.spbau.shavkunov.roguelike.gamestate.TileType


class CombatTest {
    val playerAttributes = Attributes(1, 2, 3, 4, 5)
    val monsterAttributes = Attributes(1, 10, 10, 10, 10)

    @Test
    fun combatTest() {
        val player = ActiveCharacter(TileType.Player, playerAttributes)
        val monster = ActiveCharacter(TileType.Monster, monsterAttributes)

        val resolved = CombatResolver.resolveCombat(Pair(player, monster), true)
        val damagedPlayer = resolved.first
        val damagedMonster = resolved.second

        assertEquals(1, damagedPlayer.currentAttributes.health)
        assertEquals(9,  damagedMonster.currentAttributes.health)

        val secondFight = CombatResolver.resolveCombat(Pair(damagedPlayer, damagedMonster), true)
        assertTrue(secondFight.first.isDead())
    }
}