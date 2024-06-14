package io.github.techtastic.scriptables.block

import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_ENTITY
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.screen.ScriptEditorMenu
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ScriptableBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : BlockEntity(SCRIPTABLE_BLOCK_ENTITY, blockPos, blockState),
    ExtendedScreenHandlerFactory<ScriptableBlockEntity.ScriptableBlockData>
{
    val script = mutableListOf<String>()
    val logs = mutableListOf<String>()
    private val sandbox = LuaSandbox()

    override fun createMenu(syncId: Int, inventory: Inventory, player: Player) =
        ScriptEditorMenu(syncId, inventory, ScriptableBlockData(this.blockPos, this.script))

    override fun getDisplayName() =
        Component.translatable("gui.scriptables.script_editor")

    override fun getScreenOpeningData(player: ServerPlayer?) =
        ScriptableBlockData(this.blockPos, this.script)

    fun runOnSnadbox(snippet: String) =
        this.sandbox.runScriptInSandbox(snippet)

    data class ScriptableBlockData(val pos: BlockPos, val script: List<String>) {
        constructor(buf: FriendlyByteBuf): this(buf.readBlockPos(), buf.readList(ByteBufCodecs.STRING_UTF8))

        fun toBytes(buf: FriendlyByteBuf) {
            buf.writeBlockPos(this.pos)
            buf.writeCollection(this.script, ByteBufCodecs.STRING_UTF8)
        }

        companion object {
            /*val CODEC = RecordCodecBuilder.create { instance ->
                instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(ScriptableBlockData::pos),
                    Codec.STRING.listOf().fieldOf("script").forGetter(ScriptableBlockData::script)
                ).apply(instance, ::ScriptableBlockData)
            }*/
            val STREAM_CODEC = StreamCodec.of({ buf, data -> data.toBytes(buf) }, ScriptableBlockEntity::ScriptableBlockData)
        }
    }
}