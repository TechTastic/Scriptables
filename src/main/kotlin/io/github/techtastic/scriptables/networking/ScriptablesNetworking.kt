package io.github.techtastic.scriptables.networking

import io.github.techtastic.scriptables.networking.packet.ScriptableBlockPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ScriptablesNetworking {
    fun registerGlobalListeners() {
        // Registered AbstractScriptablePacketPayload for Scriptable Block!
        PayloadTypeRegistry.playS2C().register(ScriptableBlockPayload.ID, ScriptableBlockPayload().codec())
        PayloadTypeRegistry.playC2S().register(ScriptableBlockPayload.ID, ScriptableBlockPayload().codec())
        ClientPlayNetworking.registerGlobalReceiver(ScriptableBlockPayload.ID, ScriptableBlockPayload().onClient())
        ServerPlayNetworking.registerGlobalReceiver(ScriptableBlockPayload.ID, ScriptableBlockPayload().onServer())
    }
}