package io.github.techtastic.scriptables.block.entity

import io.github.techtastic.scriptables.api.scriptable.IScriptable
import io.github.techtastic.scriptables.block.ScriptableBlockEntities.SCRIPTABLE_BLOCK_ENTITY_TYPE
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlockEntity(blockPos: BlockPos, blockState: BlockState):
    BlockEntity(SCRIPTABLE_BLOCK_ENTITY_TYPE, blockPos, blockState), IScriptable {
    var script = ""

    override fun getCode(): String = this.script

    override fun setCode(code: String) {
        this.script = code
        this.setChanged()
    }

    override fun getOrCreateEventHandler(): LuaEventHandler {
        TODO("Not yet implemented")
    }

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        compoundTag.putString("scriptables\$script", this.script)

        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)

        this.script = compoundTag.getString("scriptables\$script")
    }
}