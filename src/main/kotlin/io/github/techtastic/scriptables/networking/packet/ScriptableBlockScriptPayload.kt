package io.github.techtastic.scriptables.networking.packet

import com.mojang.serialization.Codec
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.codec.StreamEncoder
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ScriptableBlockScriptPayload(val pos: BlockPos, val script: List<String>): CustomPacketPayload {
    constructor(buf: FriendlyByteBuf): this(buf.readBlockPos(), buf.readList(ByteBufCodecs.STRING_UTF8))

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeBlockPos(this.pos)
        buf.writeCollection(this.script, ByteBufCodecs.STRING_UTF8)
    }

    override fun type() = ID

    companion object {
        val ID = CustomPacketPayload.createType<ScriptableBlockScriptPayload>("block_script")
        val CODEC = StreamCodec.of({ buf, payload -> payload.toBytes(buf) }, ::ScriptableBlockScriptPayload)
        fun onClient() = ClientPlayNetworking.PlayPayloadHandler<ScriptableBlockScriptPayload> { payload, context ->
            context.client().execute {
                val be = context.player().level().getBlockEntity(payload.pos)
                if (be is ScriptableBlockEntity)
                    be.updateScript(payload.script)
            }
        }
    }
}