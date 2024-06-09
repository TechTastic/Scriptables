package io.github.techtastic.scriptables.api.scriptable

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.BlockHitResult

interface IScriptableProvider {
    fun getScriptable(level: ServerLevel, hitResult: BlockHitResult): IScriptable?
}