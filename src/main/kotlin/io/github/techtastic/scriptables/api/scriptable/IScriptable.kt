package io.github.techtastic.scriptables.api.scriptable

import io.github.techtastic.scriptables.api.lua.LuaSandbox

interface IScriptable {
    fun getScript(): String
    fun setScript(script: String)

    fun getOrCreateSandbox(): LuaSandbox
}