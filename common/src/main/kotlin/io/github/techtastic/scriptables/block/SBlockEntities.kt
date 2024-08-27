package io.github.techtastic.scriptables.block

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.block.entity.ScriptableBE
import net.minecraft.core.registries.Registries
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.block.entity.BlockEntityType

object SBlockEntities {
    private val BLOCK_ENTITIES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val SCRIPTABLE_BLOCK = BLOCK_ENTITIES.register("scriptable_block") {
        BlockEntityType.Builder.of(::ScriptableBE, SBlocks.SCRIPTABLE_BLOCK.get()).build(null)
    }

    fun register() {
        BLOCK_ENTITIES.register()
    }
}