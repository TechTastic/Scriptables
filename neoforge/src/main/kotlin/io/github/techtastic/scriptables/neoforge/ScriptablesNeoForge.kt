package io.github.techtastic.scriptables.neoforge

import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.Scriptables.init
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(MOD_ID)
class ScriptablesNeoForge(modEventBus: IEventBus, modContainer: ModContainer) {
    init {
        init()
    }
}