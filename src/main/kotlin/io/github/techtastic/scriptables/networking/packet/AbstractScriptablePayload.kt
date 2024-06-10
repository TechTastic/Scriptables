package io.github.techtastic.scriptables.networking.packet

import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

abstract class AbstractScriptablePayload(val scriptTag: CompoundTag): CustomPacketPayload {
    abstract fun codec(): StreamCodec<out ByteBuf, out AbstractScriptablePayload>

    /**
     * Put whatever you want to happen on the Client here
     *
     * My other packets use it to sync the Script to the Clientside IScriptable, then open the ScriptEditorScreen!
     */
    abstract fun onClient(): ClientPlayNetworking.PlayPayloadHandler<out AbstractScriptablePayload>

    /**
     * Put whatever you want to happen on the Server here
     *
     * My other packets use it to sync the newly saved Script to the Serverside IScriptable, then use Script.reload()!
     */
    abstract fun onServer(): ServerPlayNetworking.PlayPayloadHandler<out AbstractScriptablePayload>
}