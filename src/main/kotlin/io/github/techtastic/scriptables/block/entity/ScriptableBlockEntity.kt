package io.github.techtastic.scriptables.block.entity

import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.scriptable.IScriptable
import io.github.techtastic.scriptables.block.ScriptableBlockEntities.SCRIPTABLE_BLOCK_ENTITY_TYPE
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlockEntity(blockPos: BlockPos, blockState: BlockState):
    BlockEntity(SCRIPTABLE_BLOCK_ENTITY_TYPE, blockPos, blockState), IScriptable {
        var savedScript = ""
    val sandbox = LuaSandbox()

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        compoundTag.putString("scriptables\$script", this.savedScript)

        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)

        this.savedScript = compoundTag.getString("scriptables\$script")
    }

    override fun getScript(): String = this.savedScript

    override fun setScript(script: String) {
        this.savedScript = script
        this.setChanged()
    }

    override fun getOrCreateSandbox() = this.sandbox
}