package io.github.techtastic.scriptables.block.custom

import io.github.techtastic.scriptables.api.lua.wrappers.LuaBlockPos
import io.github.techtastic.scriptables.block.entity.ScriptableBE
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class ScriptableBlock(properties: Properties): Block(properties), EntityBlock {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
        ScriptableBE(blockPos, blockState)

    override fun fallOn(level: Level, blockState: BlockState, blockPos: BlockPos, entity: Entity, f: Float) {
        super.fallOn(level, blockState, blockPos, entity, f)
    }

    override fun stepOn(level: Level, blockPos: BlockPos, blockState: BlockState, entity: Entity) {
        super.stepOn(level, blockPos, blockState, entity)

        val be = level.getBlockEntity(blockPos)
        if (be !is ScriptableBE) return

        val pos = LuaTable()
        pos.set("x", LuaValue.valueOf(blockPos.x))
        pos.set("y", LuaValue.valueOf(blockPos.y))
        pos.set("z", LuaValue.valueOf(blockPos.z))

        be.handleEvent("stepOn", arrayOf(pos))
    }

    override fun onProjectileHit(
        level: Level,
        blockState: BlockState,
        blockHitResult: BlockHitResult,
        projectile: Projectile
    ) {
        super.onProjectileHit(level, blockState, blockHitResult, projectile)
    }

    override fun useItemOn(
        itemStack: ItemStack,
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        interactionHand: InteractionHand,
        blockHitResult: BlockHitResult
    ): ItemInteractionResult {
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult)
    }

    override fun useWithoutItem(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        blockHitResult: BlockHitResult
    ): InteractionResult {
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
    }
}