package io.github.techtastic.scriptables.networking.packet

import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ScriptableBlockRunnablePayload(val pos: BlockPos, val snippet: String): CustomPacketPayload {
    constructor(buf: FriendlyByteBuf): this(buf.readBlockPos(), buf.readUtf())

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeBlockPos(this.pos)
        buf.writeUtf(this.snippet)
    }

    override fun type() = ID

    companion object {
        val ID = CustomPacketPayload.createType<ScriptableBlockRunnablePayload>("block_script_runnable")
        val CODEC = StreamCodec.of({ buf, payload -> payload.toBytes(buf) }, ::ScriptableBlockRunnablePayload)
        fun onServer() = ServerPlayNetworking.PlayPayloadHandler<ScriptableBlockRunnablePayload> { payload, context ->
            context.player().server.execute {
                val be = (context.player().level()).getBlockEntity(payload.pos)
                if (be is ScriptableBlockEntity)
                    be.runOnSandbox(payload.snippet)
            }
        }
    }
}