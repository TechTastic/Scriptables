package io.github.techtastic.scriptables.networking.packet

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ScriptableBlockPayload(val pos: BlockPos, scriptTag: CompoundTag): AbstractScriptablePayload(scriptTag) {
    constructor(buf: FriendlyByteBuf): this(buf.readBlockPos(), buf.readNbt() ?: CompoundTag())

    constructor(): this(BlockPos.ZERO, CompoundTag())

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeBlockPos(this.pos)
        buf.writeNbt(this.scriptTag)
    }

    override fun codec() = CustomPacketPayload.codec(ScriptableBlockPayload::toBytes, ::ScriptableBlockPayload)

    override fun onClient() =
        ClientPlayNetworking.PlayPayloadHandler<ScriptableBlockPayload> { payload, context ->
            context.client().execute {
                //val script = (context.player().level().getBlockEntity(payload.pos) as IScriptable).getScript()
                //script.loadScript(payload.scriptTag)
                //Minecraft.getInstance().setScreen(ScriptEditorScreen(script))
            }
        }

    override fun onServer() =
        ServerPlayNetworking.PlayPayloadHandler<ScriptableBlockPayload> { payload, context ->
            context.player().server.execute {
                //val script = (context.player().level().getBlockEntity(payload.pos) as IScriptable).getScript()
                //script.loadScript(payload.scriptTag)
                //script.reload()
            }
        }

    override fun type() = ID

    companion object {
        val ID = CustomPacketPayload.createType<ScriptableBlockPayload>("block_script_sync")
    }
}