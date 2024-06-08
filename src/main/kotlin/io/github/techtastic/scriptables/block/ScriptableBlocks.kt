package io.github.techtastic.scriptables.block

import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.block.custom.ScriptableBlock
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour


object ScriptableBlocks {
    val SCRIPTABLE_BLOCK = registerBlockWithItem("scriptable_block", ScriptableBlock(BlockBehaviour.Properties.of()))

    fun init() {
        LOGGER.info("Registering $MOD_ID Blocks!")
    }

    private fun registerBlockWithItem(id: String, block: Block): Block {
        Registry.register(BuiltInRegistries.ITEM, Scriptables.getWithModId(id), BlockItem(block, Item.Properties()))
        return Registry.register(BuiltInRegistries.BLOCK, Scriptables.getWithModId(id), block)
    }
}