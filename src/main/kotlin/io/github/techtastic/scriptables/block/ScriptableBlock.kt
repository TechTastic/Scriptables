package io.github.techtastic.scriptables.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlock(properties: Properties) : Block(properties), EntityBlock {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
        ScriptableBlockEntity(blockPos, blockState)
}