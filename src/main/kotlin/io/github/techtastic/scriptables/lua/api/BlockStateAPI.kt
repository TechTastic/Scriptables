package io.github.techtastic.scriptables.lua.api

import io.github.techtastic.scriptables.api.lua.ILuaAPI
import net.minecraft.world.level.block.state.BlockState

class BlockStateAPI(state: BlockState): ILuaAPI {
    override fun getName() = "block"
}