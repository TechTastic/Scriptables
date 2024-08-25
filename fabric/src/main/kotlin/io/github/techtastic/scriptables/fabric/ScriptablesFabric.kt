package io.github.techtastic.scriptables.fabric

import io.github.techtastic.scriptables.Scriptables.init
import net.fabricmc.api.ModInitializer

object ScriptablesFabric: ModInitializer {
    override fun onInitialize() {
        init()
    }
}