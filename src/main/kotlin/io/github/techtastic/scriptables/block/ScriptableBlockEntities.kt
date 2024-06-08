package io.github.techtastic.scriptables.block

import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.block.entity.ScriptableBlockEntity
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object ScriptableBlockEntities {
    val SCRIPTABLE_BLOCK_ENTITY_TYPE = registerBlockEntityType("scriptable_block", BlockEntityType.Builder.of(
        ::ScriptableBlockEntity,
        ScriptableBlocks.SCRIPTABLE_BLOCK
    ).build(null))

    fun init() {
        Scriptables.LOGGER.info("Registering ${Scriptables.MOD_ID} Block Entities!")
    }

    private fun registerBlockEntityType(id: String, type: BlockEntityType<*>): BlockEntityType<*> {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type)
    }
}