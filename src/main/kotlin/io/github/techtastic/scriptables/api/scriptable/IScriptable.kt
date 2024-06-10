package io.github.techtastic.scriptables.api.scriptable

import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script
import net.minecraft.server.level.ServerPlayer

interface IScriptable {
    fun getScript(): Script

    fun getOrCreateSandbox(): LuaSandbox

    fun openScreenOnClient(player: ServerPlayer)
}