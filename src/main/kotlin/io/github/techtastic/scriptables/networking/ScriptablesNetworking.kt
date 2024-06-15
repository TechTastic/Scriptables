package io.github.techtastic.scriptables.networking

import io.github.techtastic.scriptables.networking.packet.ScriptableBlockLogsPayload
import io.github.techtastic.scriptables.networking.packet.ScriptableBlockRunnablePayload
import io.github.techtastic.scriptables.networking.packet.ScriptableBlockScriptPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ScriptablesNetworking {
    fun registerGlobalListeners() {
        // Registered AbstractScriptablePacketPayload for Scriptable Block!
        //PayloadTypeRegistry.playS2C().register(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload().codec())
        //ClientPlayNetworking.registerGlobalReceiver(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload().onClient())

        PayloadTypeRegistry.playC2S().register(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload.onServer())

        PayloadTypeRegistry.playS2C().register(ScriptableBlockLogsPayload.ID, ScriptableBlockLogsPayload.CODEC)
        ClientPlayNetworking.registerGlobalReceiver(ScriptableBlockLogsPayload.ID, ScriptableBlockLogsPayload.onClient())

        PayloadTypeRegistry.playS2C().register(ScriptableBlockScriptPayload.ID, ScriptableBlockScriptPayload.CODEC)
        ClientPlayNetworking.registerGlobalReceiver(ScriptableBlockScriptPayload.ID, ScriptableBlockScriptPayload.onClient())
    }
}