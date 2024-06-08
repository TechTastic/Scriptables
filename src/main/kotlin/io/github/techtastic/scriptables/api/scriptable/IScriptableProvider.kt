package io.github.techtastic.scriptables.api.scriptable

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

interface IScriptableProvider {
    fun getScriptable(level: ServerLevel, pos: BlockPos, entity: Entity): IScriptable?
}