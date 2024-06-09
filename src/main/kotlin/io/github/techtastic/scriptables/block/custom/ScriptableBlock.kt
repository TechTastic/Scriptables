package io.github.techtastic.scriptables.block.custom

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.github.techtastic.scriptables.block.entity.ScriptableBlockEntity
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.item.ScriptableItems.SCRIPTER
import io.github.techtastic.scriptables.screen.ScriptEditorScreen
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class ScriptableBlock(properties: Properties): Block(properties), EntityBlock {
    val sandbox = LuaSandbox()
    var script: String = "print('Ran Script!')"

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
        ScriptableBlockEntity(blockPos, blockState)

    override fun useItemOn(
        itemStack: ItemStack,
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        interactionHand: InteractionHand,
        blockHitResult: BlockHitResult
    ): ItemInteractionResult {
        if (itemStack.`is`(SCRIPTER))
            Minecraft.getInstance().setScreen(ScriptEditorScreen("print('Ran Script!')"))

        this.sandbox.runScriptInSandbox("print('Ran Script!')")

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult)
    }
}