package io.github.techtastic.scriptables.block

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.block.custom.ScriptableBlock
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour

object SBlocks {
    private val BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK)
    private val BLOCK_ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM)

    val SCRIPTABLE_BLOCK = BLOCKS.register("scriptable_block") {
        ScriptableBlock(BlockBehaviour.Properties.of())
    }

    fun register() {
        BLOCKS.register()

        BLOCKS.forEach { block -> BLOCK_ITEMS.register(block.id) {
            BlockItem(block.get(), Item.Properties())
        } }

        BLOCK_ITEMS.register()
    }
}