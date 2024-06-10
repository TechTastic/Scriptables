package io.github.techtastic.scriptables.api.scriptable

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.Optional

interface IScriptableProvider {
    fun getScriptable(level: Level, player: Player, hand: InteractionHand): Optional<IScriptable>
}