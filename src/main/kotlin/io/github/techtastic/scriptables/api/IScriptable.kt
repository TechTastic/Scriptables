package io.github.techtastic.scriptables.api

import io.github.techtastic.scriptables.api.lua.LuaSandbox
import org.luaj.vm2.lib.TwoArgFunction

interface IScriptable {
    fun getScript(): List<String>

    fun getScriptAsString(): String {
        var script = ""
        this.getScript().forEach { line ->
            script += line + "\n"
        }
        return script
    }

    fun getLogs():  MutableList<Pair<Boolean, String>>

    fun updateScript(newScipt: List<String>)

    fun getSandbox(): LuaSandbox

    fun addCustomLibraries(libraries: MutableList<TwoArgFunction>)

    fun runScript() {
        val libraries = mutableListOf<TwoArgFunction>()
        this.addCustomLibraries(libraries)
        this.getSandbox().runScriptInSandbox(this.getScriptAsString(), libraries, this.getLogs())
    }

    fun runSnippet(snippet: String) {
        val libraries = mutableListOf<TwoArgFunction>()
        this.addCustomLibraries(libraries)
        this.getSandbox().runScriptInSandbox(snippet, libraries, this.getLogs())
    }
}