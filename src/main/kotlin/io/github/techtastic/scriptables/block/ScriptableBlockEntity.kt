package io.github.techtastic.scriptables.block

import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_ENTITY
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.api.scriptable.IScriptable
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(SCRIPTABLE_BLOCK_ENTITY, blockPos, blockState), IScriptable {
        private val script = object: Script() {
            override fun reload() {
                this@ScriptableBlockEntity.setChanged()
            }
        }
    private val sandbox = LuaSandbox()

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        this.script.saveScript(compoundTag)

        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)

        this.script.loadScript(compoundTag)
    }

    override fun getScript() = this.script

    override fun getOrCreateSandbox() = this.sandbox
}