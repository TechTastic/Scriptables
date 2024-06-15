package io.github.techtastic.scriptables.block

import com.mojang.serialization.Codec
import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_ENTITY
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.screen.ScriptEditorMenu
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
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
    val script = mutableListOf(
        "print('Hello')",
        "print('World')"
    )
    val logs = mutableListOf<Pair<Boolean, String>>()
    private val sandbox = LuaSandbox()

    init {
        this.runOnSandbox(getFormattedScript(this.script))
    }

    override fun createMenu(syncId: Int, inventory: Inventory, player: Player) =
        ScriptEditorMenu(syncId, inventory, ScriptableBlockData(this.blockPos, this.script))

    override fun getDisplayName() =
        Component.translatable("gui.scriptables.script_editor")

    override fun getScreenOpeningData(player: ServerPlayer?) =
        ScriptableBlockData(this.blockPos, this.script)

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        val list = ListTag()
        this.script.forEach { line ->
            list.add(Codec.STRING.write(NbtOps.INSTANCE, line))
        }
        compoundTag.put("scriptables\$script", list)
        compoundTag.putInt("scriptables\$lineCount", list.size)

        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)

        this.script.clear()
        compoundTag.getList("scriptables\$script", compoundTag.getInt("scriptables\$lineCount")).forEach { tag ->
            this.script.add(Codec.STRING.read(NbtOps.INSTANCE, tag).orThrow)
        }
    }

    fun updateScript(lines: List<String>) {
        this.script.clear()
        this.script.addAll(lines)
        this.setChanged()
    }

    override fun setChanged() {
        super.setChanged()
        this.runOnSandbox(getFormattedScript(this.script))
    }

    fun runOnSandbox(snippet: String) =
        this.sandbox.runScriptInSandbox(snippet, this.logs)

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

    companion object {
        fun getFormattedScript(script: List<String>): String {
            var code = ""
            script.forEach {
                code += it + "\n"
            }
            return code
        }
    }
}