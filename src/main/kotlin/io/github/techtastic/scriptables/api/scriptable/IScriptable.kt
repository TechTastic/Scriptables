package io.github.techtastic.scriptables.api.scriptable

import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script

interface IScriptable {
    fun getScript(): Script

    fun getOrCreateSandbox(): LuaSandbox
}