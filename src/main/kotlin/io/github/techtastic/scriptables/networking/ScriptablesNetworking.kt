package io.github.techtastic.scriptables.networking

import io.github.techtastic.scriptables.networking.packet.ScriptableBlockRunnablePayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ScriptablesNetworking {
    fun registerGlobalListeners() {
        // Registered AbstractScriptablePacketPayload for Scriptable Block!
        //PayloadTypeRegistry.playS2C().register(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload().codec())
        //ClientPlayNetworking.registerGlobalReceiver(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload().onClient())

        PayloadTypeRegistry.playC2S().register(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(ScriptableBlockRunnablePayload.ID, ScriptableBlockRunnablePayload.onServer())
    }
}