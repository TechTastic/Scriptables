package io.github.techtastic.scriptables.api

import io.github.techtastic.scriptables.api.scriptable.IScriptable
import io.github.techtastic.scriptables.api.scriptable.IScriptableProvider
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrElse

object ScriptablesAPI {
    private val providers = mutableListOf<IScriptableProvider>()

    fun registerScriptableProvider(provider: IScriptableProvider) {
        this.providers.add(provider)
    }

    fun findScriptable(level: Level, player: Player, hand: InteractionHand): IScriptable? {
        this.providers.forEach { provider ->
            return provider.getScriptable(level, player, hand).getOrElse { return@forEach }
        }
        return null
    }
}