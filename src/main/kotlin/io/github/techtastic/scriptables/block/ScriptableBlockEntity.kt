package io.github.techtastic.scriptables.block

import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_ENTITY
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.api.scriptable.IScriptable
import io.github.techtastic.scriptables.networking.packet.ScriptableBlockPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(SCRIPTABLE_BLOCK_ENTITY, blockPos, blockState), IScriptable {
        private val script = object: Script() {
            override fun reload() {
                if (Minecraft.getInstance() == null)
                    this@ScriptableBlockEntity.setChanged()
                else {
                    val scriptTag = CompoundTag()
                    this.saveScript(scriptTag)
                    ClientPlayNetworking.send(ScriptableBlockPayload(this@ScriptableBlockEntity.blockPos, scriptTag))
                }
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

    override fun openScreenOnClient(player: ServerPlayer) {
        val tag = CompoundTag()
        this.script.saveScript(tag)
        ServerPlayNetworking.send(player, ScriptableBlockPayload(this.blockPos, tag))
    }
}