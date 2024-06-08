package io.github.techtastic.scriptables.api.scriptable

import io.github.techtastic.scriptables.api.LuaEventHandler

interface IScriptable {
    fun getCode(): String
    fun setCode(code: String)

    fun getOrCreateEventHandler(): LuaEventHandler
}