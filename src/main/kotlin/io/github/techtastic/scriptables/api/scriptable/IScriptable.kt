package io.github.techtastic.scriptables.api.scriptable

import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import org.luaj.vm2.Globals

interface IScriptable {
    fun getScript(): Script

    fun getLog(): List<String>

    fun getOrCreateSandbox(): LuaSandbox

    fun openScreenOnClient(player: ServerPlayer)

    fun addCustomLibraries(userGlobals: Globals)

    fun getMenuProvider(): MenuProvider
}