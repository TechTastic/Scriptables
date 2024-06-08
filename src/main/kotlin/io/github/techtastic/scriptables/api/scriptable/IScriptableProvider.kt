package io.github.techtastic.scriptables.api.scriptable

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

interface IScriptableProvider {
    fun getScriptable(level: ServerLevel, pos: BlockPos): IScriptable?
}