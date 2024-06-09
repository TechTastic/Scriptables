package io.github.techtastic.scriptables.api.scriptable

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import java.util.Optional

interface IScriptableProvider {
    fun getScriptable(level: Level, pos: BlockPos, entity: Entity?): Optional<IScriptable>
}