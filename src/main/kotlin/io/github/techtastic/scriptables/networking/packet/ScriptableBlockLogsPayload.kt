package io.github.techtastic.scriptables.networking.packet

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.codec.StreamEncoder
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ScriptableBlockLogsPayload(val pos: BlockPos, val logs: List<Pair<Boolean, String>>): CustomPacketPayload {
    constructor(buf: FriendlyByteBuf): this(buf.readBlockPos(), buf.readList { internalBuf ->
        Pair(internalBuf.readBoolean(), internalBuf.readUtf())
    })

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeBlockPos(this.pos)
        buf.writeCollection(this.logs) { internalBuf, pair ->
            internalBuf.writeBoolean(pair.first)
            internalBuf.writeUtf(pair.second)
        }
    }

    override fun type() = ID

    companion object {
        val ID = CustomPacketPayload.createType<ScriptableBlockLogsPayload>("block_logs")
        val CODEC = StreamCodec.of({ buf, payload -> payload.toBytes(buf) }, ::ScriptableBlockLogsPayload)
        fun onClient() = ClientPlayNetworking.PlayPayloadHandler<ScriptableBlockLogsPayload> { payload, context ->
            context.client().execute {
                val be = context.player().level().getBlockEntity(payload.pos)
                if (be is ScriptableBlockEntity) {
                    be.logs.clear()
                    be.logs.addAll(payload.logs)
                    LOGGER.info(be.logs.toString())
                }
            }
        }
    }
}