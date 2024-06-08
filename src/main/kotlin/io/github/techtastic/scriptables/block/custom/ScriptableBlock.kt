package io.github.techtastic.scriptables.block.custom

import io.github.techtastic.scriptables.block.entity.ScriptableBlockEntity
import io.github.techtastic.scriptables.lua.LuaSandbox
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class ScriptableBlock(properties: Properties): Block(properties), EntityBlock {
    val sandbox = LuaSandbox()

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
        ScriptableBlockEntity(blockPos, blockState)

    override fun useWithoutItem(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        blockHitResult: BlockHitResult
    ): InteractionResult {
        this.sandbox.runScriptInSandbox("require('test').testing()")

        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
    }
}